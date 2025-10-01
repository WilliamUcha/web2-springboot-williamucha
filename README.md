#  EduSystem - Sistema de Gerenciamento de Cursos

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.0-purple)
![Status](https://img.shields.io/badge/Status-Ativo-success)

##  Sobre o Projeto

O **EduSystem** é uma plataforma completa para gerenciamento de cursos online, desenvolvida em Java com Spring Boot. O sistema permite o cadastro e gerenciamento de **Professores**, **Categorias** e **Cursos** de forma moderna e eficiente.

###  Funcionalidades Principais

-  **Gestão de Professores** - Cadastro completo com upload de imagem
-  **Gestão de Categorias** - Organização dos cursos por áreas de conhecimento
-  **Gestão de Cursos** - Criação e administração de conteúdo educacional
-  **Interface Pública** - Navegação por categorias e visualização de cursos
-  **Design Responsivo** - Interface adaptável para todos os dispositivos

##  Demonstração

###  Vídeo de Funcionamento

[![🎬 Demonstração Completa do EduSystem](https://img.shields.io/badge/▶️%20Assistir%20Demo-YouTube-red?style=for-the-badge&logo=youtube&logoColor=white)](https://youtu.be/ydEc05PGGP0)

> ** Assista à demonstração completa no YouTube!**  
> O vídeo mostra todas as funcionalidades do sistema em funcionamento.


</details>


### Executando o Projeto

1. **Clone o repositório:**
```bash
git clone https://github.com/WilliamUcha/2025web2spring-main.git
cd 2025web2spring-main/web2/web2
```

2. **Execute com Maven:**
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

3. **Acesse a aplicação:**
```
http://localhost:8080
```

##  Estrutura do Projeto

```
web2/
├── src/
│   ├── main/
│   │   ├── java/com/web2/
│   │   │   ├── controller/          # Controladores MVC
│   │   │   ├── model/              # Entidades JPA
│   │   │   ├── repository/         # Repositories
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   └── Web2Application.java
│   │   └── resources/
│   │       ├── templates/          # Templates Thymeleaf
│   │       │   ├── categoria/     # CRUD Categorias
│   │       │   ├── curso/         # CRUD Cursos  
│   │       │   ├── professor/     # CRUD Professores
│   │       │   ├── index.html     # Página inicial
│   │       │   └── layout.html    # Layout base
│   │       ├── static/
│   │       │   └── img/          # Imagens dos professores
│   │       └── application.properties
│   └── test/                      # Testes
├── target/                        # Build artifacts
├── pom.xml                       # Dependências Maven
└── README.md                     # Este arquivo
```

##  Funcionalidades Detalhadas

###  Gestão de Professores
-  Cadastro com campos: nome, email, telefone, especialização, currículo
-  Upload de foto do professor
-  Listagem com busca e paginação
-  Edição e exclusão com validações
-  Prevenção de exclusão com cursos vinculados

###  Gestão de Categorias  
-  Cadastro com nome e descrição
-  Listagem organizada
-  Edição e exclusão
-  Prevenção de exclusão com cursos vinculados

###  Gestão de Cursos
-  Cadastro completo: nome, descrição, carga horária, preço, datas
-  Vinculação com professor e categoria
-  Listagem administrativa
-  Edição e exclusão
-  Ícones padronizados 

###  Interface Pública
-  Página inicial com cards de navegação
-  Listagem de cursos por categoria
-  Página de detalhes do curso
-  Informações do professor responsável
-  Design responsivo e moderno





##  Como Contribuir
## Autor

**William Ucha**
- GitHub: [@WilliamUcha](https://github.com/WilliamUcha)
