# Sistema de Apoio a Testes Exploratórios em Video Games

# Descrição
Este projeto tem como objetivo o desenvolvimento de uma aplicação web para apoiar testadores de jogos digitais na condução de Testes Exploratórios (TE). A aplicação permite o gerenciamento de projetos de teste, cadastro e visualização de estratégias adaptadas para jogos, bem como o acompanhamento das sessões de teste.

# Funcionalidades
* Perfis de usuário: Administrador, Testador e Visitante, com permissões hierárquicas.
* Gestão de Projetos: Criar, listar e editar projetos (nome, descrição, data de criação e membros).
* Sessões de Teste: Criar, executar e finalizar sessões com controle de tempo e ciclo de vida.
* Registro de Bugs: Durante a execução de sessões.
* Cadastro de Estratégias: Nome, descrição, exemplos, dicas e imagens.
* Visualização Pública de Estratégias: Visitantes podem explorar estratégias cadastradas.
* Internacionalização: Sistema disponível em Português e Inglês.
* Tratamento de Erros: Páginas amigáveis e registros de log.
* Controle de Acesso: Implementado com Spring Security.

# Requisitos Funcionais
| Código | Descrição                                                 | Requer Login      |
| ------ | --------------------------------------------------------- | ----------------- |
| R1     | CRUD de testadores                                        | Administrador     |
| R2     | CRUD de administradores                                   | Administrador     |
| R3     | Cadastro de projetos                                      | Administrador     |
| R4     | Listagem e ordenação de projetos                          | Depende do perfil |
| R5     | Cadastro de estratégias                                   | Administrador     |
| R6     | Listagem pública de estratégias                           | Não               |
| R7     | Cadastro de sessões de teste                              | Testador          |
| R8     | Ciclo de vida da sessão (criado, em execução, finalizado) | Testador          |
| R9     | Listagem de sessões por projeto com permissões            | Depende do perfil |
| R10    | Internacionalização PT/EN                                 | Não               |

# Arquitetura
O sistema segue a arquitetura Modelo-Visão-Controlador (MVC), utilizando as seguintes tecnologias:

# Lado Servidor
* Spring MVC
* Spring Data JPA
* Spring Security
* Thymeleaf

# Lado Cliente
* HTML
* CSS
* JavaScript
=======
# AA3

O sistema foi transformado em uma  API RESTful, desacoplando a lógica de negócios do backend do frontend. 

A implementação seguiu uma arquitetura em camadas, utilizando as tecnologias centrais do Spring:

 * Controladores (Controllers): Foram criados RestControllers para cada recurso principal da aplicação, como ProjetoAdminController, UsuarioController, EstrategiaAdminController e SessaoController. Cada controlador tem endpoints HTTP (GET, POST, PUT, DELETE) para realizar as operações de CRUD sobre os recursos.
 * Segurança com JWT (JSON Web Tokens): A segurança da API foi implementada utilizando Spring Security com autenticação baseada em token JWT. As regras de autorização foram definidas no WebSecurityConfig, restringindo o acesso a endpoints específicos com base nos papéisndo usuário (ex: ADMIN, TESTER).
 * Abstração com DTO: Para proteger a estrutura interna do banco de dados e otimizar os dados trafegados, foram criados DTOs para cada entidade (ex: ProjetoDTO, UsuarioDTO). A classe EntityMapper é responsável por fazer a conversão entre as entidades do banco de dados e seus respectivos DTOs.
 * Camada de Serviço e Repositório: A lógica de negócios, como salvar um projeto e associar seus testadores, permanece na camada de Serviço (Services). A camada de Repositório (Repositories) continua responsável pela persistência dos dados, utilizando Spring Data JPA para interagir com o banco de dados PostgreSQL.
