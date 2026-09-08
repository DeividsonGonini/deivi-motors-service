# Cria ECR - Elastic Container Registry
resource "aws_ecr_repository" "deivimotors_service" {
  name                 = "deivimotors-service"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

# Politica de Lifecycle, mantendo apenas as ultimas 10 imagens
resource "aws_ecr_lifecycle_policy" "deivimotors_service" {
  repository = aws_ecr_repository.deivimotors_service.name

  policy = jsonencode({
    rules = [
      {
        rulePriority = 1
        description  = "Manter somente as 10 imagens mais recentes"
        selection = {
          tagStatus   = "any"
          countType   = "imageCountMoreThan"
          countNumber = 10
        }
        action = {
          type = "expire"
        }
      }
    ]
  })
}



# WEBHOOK
# Cria ECR
resource "aws_ecr_repository" "webhook" {
  name                 = "webhook"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

# Politica de Lifecycle, mantendo apenas as ultimas 10 imagens
resource "aws_ecr_lifecycle_policy" "webhook" {
  repository = aws_ecr_repository.webhook.name

  policy = jsonencode({
    rules = [
      {
        rulePriority = 1
        description  = "Manter somente as 10 imagens mais recentes"
        selection = {
          tagStatus   = "any"
          countType   = "imageCountMoreThan"
          countNumber = 10
        }
        action = {
          type = "expire"
        }
      }
    ]
  })
}