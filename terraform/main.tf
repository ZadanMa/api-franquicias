provider "aws" {
region = var.aws_region
}

variable "aws_region" {
description = "Región de AWS para el despliegue"
type        = string
default     = "us-east-1"
}

variable "vpc_id" {
description = "ID de la VPC existente"
type        = string
default     = "vpc-04a0d9f6efd8cea41"
}

variable "subnet_ids" {
description = "IDs de las subredes existentes para ECS y ALB"
type        = list(string)
default     = ["subnet-03b0a4d0333a3419e", "subnet-0dab25d7b8dcb5c72"]
}

variable "ecs_security_group_id" {
description = "ID del Security Group para la tarea ECS (EC2)"
type        = string
default     = "sg-0dc8c5f41152d59e3"
}

variable "alb_security_group_id" {
description = "ID del Security Group para el ALB"
type        = string
default     = "sg-02e114cb1e17190d0"
}

variable "docdb_security_group_id" {
description = "ID del Security Group para DocumentDB"
type        = string
default     = "sg-065c7711075dc51db"
}

variable "docdb_cluster_identifier" {
description = "Identificador del clúster de DocumentDB"
type        = string
default     = "document1"
}

variable "docdb_engine_version" {
description = "Versión del motor de DocumentDB (compatible con MongoDB)"
type        = string
default     = "5.0.0"
}

variable "docdb_instance_class" {
description = "Tipo de instancia para DocumentDB"
type        = string
default     = "db.t3.medium"
}

variable "docdb_database_name" {
description = "Nombre de la base de datos en DocumentDB"
type        = string
default     = "franquiciasdb"
}

variable "ecs_instance_type" {
description = "Tipo de instancia para ECS"
type        = string
default     = "t3.medium"
}

variable "key_name" {
description = "Nombre del KeyPair existente para acceso SSH a EC2"
type        = string
default     = "hola"
}

variable "ecr_image" {
description = "URI de la imagen en ECR para la aplicación"
type        = string
default     = "public.ecr.aws/j4e9h3n0/api-franquicias-terraform:latest"
}

data "aws_ssm_parameter" "db_username" {
name = "/ecs/api-franquicias/DB_USERNAME"
}

data "aws_ssm_parameter" "db_password" {
name = "/ecs/api-franquicias/DB_PASSWORD"
}

data "aws_ssm_parameter" "db_puerto" {
name = "/ecs/api-franquicias/DB_PUERTO"
}

data "aws_ssm_parameter" "app_port" {
name = "/ecs/api-franquicias/PORT"
}

variable "ecs_task_execution_role_arn" {
description = "ARN del IAM Role existente para ECS Task Execution"
type        = string
default     = "arn:aws:iam::340752805522:role/ecsTaskExecutionRole"
}

resource "null_resource" "wait_for_ecs_instances" {
depends_on = [aws_autoscaling_group.ecs_asg]

provisioner "local-exec" {
command = <<EOF
      aws autoscaling describe-auto-scaling-groups --auto-scaling-group-names ${aws_autoscaling_group.ecs_asg.name} --query 'AutoScalingGroups[0].Instances[].InstanceId' --output text | xargs -n1 aws ec2 describe-instances --instance-ids | jq '.Reservations[].Instances[] | select(.State.Name == "running") | .InstanceId'
    EOF
interpreter = ["bash", "-c"]
}

triggers = {
asg_name = aws_autoscaling_group.ecs_asg.name
}
}

resource "aws_docdb_subnet_group" "docdb_subnet_group" {
name        = "franquicias-docdb-subnet-group"
description = "Grupo de subredes para el clúster DocumentDB"
subnet_ids  = var.subnet_ids
}

resource "aws_docdb_cluster" "docdb_cluster" {
  cluster_identifier      = var.docdb_cluster_identifier
  engine                  = "docdb"
  engine_version          = var.docdb_engine_version
  master_username         = data.aws_ssm_parameter.db_username.value
  master_password         = data.aws_ssm_parameter.db_password.value
  db_subnet_group_name    = aws_docdb_subnet_group.docdb_subnet_group.name
  vpc_security_group_ids  = [var.docdb_security_group_id]
  storage_encrypted       = true
  backup_retention_period = 7
  preferred_backup_window = "07:00-09:00"
  skip_final_snapshot = true

  tags = {
  Name = var.docdb_cluster_identifier
  }
}

resource "aws_docdb_cluster_instance" "docdb_instance" {
count                = 1
cluster_identifier   = aws_docdb_cluster.docdb_cluster.cluster_identifier
instance_class       = var.docdb_instance_class
engine               = "docdb"

tags = {
Name = "${var.docdb_cluster_identifier}-instance-1"
}
}

resource "aws_ecs_cluster" "ecs_cluster" {
name = "api-franquicias-ecs-cluster"
}

resource "aws_ecs_task_definition" "ecs_task_definition" {
family                   = "api-franquicias-task"
network_mode             = "awsvpc"
requires_compatibilities = ["EC2"]
cpu                      = "512"
memory                   = "850"
execution_role_arn       = var.ecs_task_execution_role_arn

container_definitions = jsonencode([
{
name = "api-franquicias-container",
image = var.ecr_image,
portMappings = [
{ containerPort = 8080, hostPort = 8080, protocol = "tcp" }
],
environment = [
{ name = "DATABASE_NAME", value = var.docdb_database_name }
],
secrets = [
{
"name": "DB_USERNAME",
"valueFrom": data.aws_ssm_parameter.db_username.arn
},
{
"name": "DB_PASSWORD",
"valueFrom": data.aws_ssm_parameter.db_password.arn
},
{
"name": "DB_PUERTO",
"valueFrom": data.aws_ssm_parameter.db_puerto.arn
},
{
"name": "PORT",
"valueFrom": data.aws_ssm_parameter.app_port.arn
}
]
}
])
}

data "aws_caller_identity" "current" {}

resource "aws_launch_template" "ecs_launch_template" {
name_prefix          = "ecs-launch-template-"
image_id             = data.aws_ami.ecs_optimized.id
instance_type        = var.ecs_instance_type
key_name             = var.key_name
iam_instance_profile {
name = aws_iam_instance_profile.ecs_instance_profile.name
}
vpc_security_group_ids = [var.ecs_security_group_id]

user_data = base64encode(<<EOF
#!/bin/bash
echo ECS_CLUSTER=${aws_ecs_cluster.ecs_cluster.name} >> /etc/ecs/ecs.config
EOF
)
}

data "aws_ami" "ecs_optimized" {
most_recent = true
owners      = ["amazon"]
filter {
name   = "name"
values = ["amzn2-ami-ecs-hvm-2.0.????????-x86_64-ebs"]
}
}
resource "aws_iam_instance_profile" "ecs_instance_profile" {
name = "ecsInstanceProfile"
role = "ecsInstanceRole"
}

resource "aws_autoscaling_group" "ecs_asg" {
name                      = "ecs-asg"
desired_capacity          = 1
max_size                  = 1
min_size                  = 1
vpc_zone_identifier       = var.subnet_ids
launch_template {
id      = aws_launch_template.ecs_launch_template.id
version = "$Latest"
}
force_delete              = true

tag {
key                 = "Name"
value               = "api-franquicias-ecs-instance"
propagate_at_launch = true
}
}

resource "aws_lb" "ecs_alb" {
name               = "api-franquicias-alb"
internal           = false
load_balancer_type = "application"
security_groups    = [var.alb_security_group_id]
subnets            = var.subnet_ids
}

resource "aws_lb_target_group" "ecs_tg" {
name        = "api-franquicias-tg"
port        = 8080
protocol    = "HTTP"
vpc_id      = var.vpc_id
target_type = "ip"
}

resource "aws_lb_listener" "ecs_listener" {
load_balancer_arn = aws_lb.ecs_alb.arn
port              = "80"
protocol          = "HTTP"

default_action {
type             = "forward"
target_group_arn = aws_lb_target_group.ecs_tg.arn
}
}

resource "aws_ecs_service" "ecs_service" {
name            = "api-franquicias-service"
cluster         = aws_ecs_cluster.ecs_cluster.id
task_definition = aws_ecs_task_definition.ecs_task_definition.arn
desired_count   = 1
launch_type     = "EC2"

network_configuration {
subnets         = var.subnet_ids
assign_public_ip = false
security_groups = [var.ecs_security_group_id]
}

load_balancer {
target_group_arn = aws_lb_target_group.ecs_tg.arn
container_name   = "api-franquicias-container"
container_port   = 8080
}

depends_on = [null_resource.wait_for_ecs_instances]
}

output "DocumentDBEndpoint" {
description = "Endpoint de DocumentDB"
value       = aws_docdb_cluster.docdb_cluster.endpoint
}

output "ALBDNS" {
description = "DNS del ALB para acceder a la API"
value       = aws_lb.ecs_alb.dns_name
}

output "ECSClusterName" {
description = "Nombre del clúster ECS"
value       = aws_ecs_cluster.ecs_cluster.name
}