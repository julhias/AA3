<<<<<<< HEAD

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

