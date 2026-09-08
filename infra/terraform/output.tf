output "ecr_deivimotors_service_url" {
  description = "URL do repositorio ECR do deivimotors-service"
  value       = aws_ecr_repository.deivimotors_service.repository_url
}

output "ecr_webhook_url" {
  description = "URL do repositorio ECR do webhook"
  value       = aws_ecr_repository.webhook.repository_url
}