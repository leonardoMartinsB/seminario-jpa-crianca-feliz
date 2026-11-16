# Criança Feliz em Java e Spring Boot

Leonardo Martins Brasilio e Pedro Henrique Menezes Bernardo · G10

Técnicas Avançadas de Programação · Seminário JPA e Projeto Integrador

Este módulo do Projeto Integrador demonstra o cadastro de responsáveis e assistidos e o registro de fichas socioeconômicas. O domínio vem do sistema da Associação Criança Feliz de Sorocaba. A versão acadêmica usa dados fictícios e um banco H2 em memória, separado da aplicação PHP publicada.

## Execução local

Requisitos de execução: **JDK 21** e **Maven 3.9+**.

~~~sh
git clone https://github.com/leonardoMartinsB/seminario-jpa-crianca-feliz.git
cd seminario-jpa-crianca-feliz
mvn spring-boot:run
~~~

A API escuta em `http://127.0.0.1:8081/api`. Cada inicialização cria três assistidos fictícios, um responsável compartilhado por dois deles e uma ficha vinculada ao primeiro assistido. A terceira pessoa começa sem responsável. Os dados são recriados quando o processo reinicia.

O [console H2](http://localhost:8081/h2-console) usa JDBC URL `jdbc:h2:mem:criancafeliz;DB_CLOSE_DELAY=-1`, usuário `sa` e senha em branco. Use o ambiente local com dados fictícios. O arquivo [consultas.sql](demo/consultas.sql) mostra como verificar as chaves e os vínculos.

## Arquitetura

`Controller` recebe HTTP e aplica Bean Validation nos DTOs de entrada. `Service` aplica as regras dentro de transações e usa repositórios `JpaRepository`. As entidades representam as tabelas e relações no Hibernate. `FichaMapper` converte os resultados em DTOs para o Controller retornar JSON. Os resumos de vínculo incluem ID e nome, sem percorrer coleções reversas.

## Modelo de dados

~~~mermaid
erDiagram
    RESPONSAVEIS |o--o{ ATENDIDOS : "é responsável por"
    ATENDIDOS ||--o| FICHAS_SOCIOECONOMICAS : "possui"
    RESPONSAVEIS {
        bigint id PK
        varchar nome
        varchar telefone
    }
    ATENDIDOS {
        bigint id PK
        varchar nome
        date data_nascimento
        bigint responsavel_id FK "opcional"
    }
    FICHAS_SOCIOECONOMICAS {
        bigint id PK
        bigint atendido_id FK,UK "obrigatória"
        decimal renda_familiar
        int pessoas_no_domicilio
    }
~~~

- **Responsável e atendido.** Um responsável pode acompanhar vários atendidos. O atendido pode começar sem responsável. `Atendido.responsavel` é o lado proprietário e grava `ATENDIDOS.RESPONSAVEL_ID`. `Responsavel.atendidos` usa `@OneToMany(mappedBy = "responsavel")` para a navegação inversa.
- **Atendido e ficha.** Cada ficha pertence a um atendido e cada atendido pode ter até uma ficha. `FichaSocioeconomica.atendido` é o lado proprietário do `@OneToOne` unidirecional. A FK `FICHAS_SOCIOECONOMICAS.ATENDIDO_ID` é obrigatória e única. A consulta no sentido inverso é feita pelo repositório.
- **Integridade.** Uma ficha existente preserva o vínculo com o atendido original. A exclusão de um responsável com atendidos, de um atendido com ficha ou a criação de uma segunda ficha retornam `409`. A renda por pessoa é calculada a partir da renda familiar e da quantidade de moradores. Este recorte usa chaves estrangeiras diretas.

## Endpoints

| Recurso | Coleção | Item |
|---|---|---|
| Responsável | `GET/POST /api/responsaveis` | `GET/PUT/DELETE /api/responsaveis/{id}` |
| Atendido | `GET/POST /api/atendidos` | `GET/PUT/DELETE /api/atendidos/{id}` |
| Ficha | `GET/POST /api/fichas-socioeconomicas` | `GET/PUT/DELETE /api/fichas-socioeconomicas/{id}` |

Consultas relacionais: `GET /api/responsaveis/{id}/atendidos` e `GET /api/atendidos/{id}/ficha-socioeconomica`.

Os códigos principais são `200` para leitura e atualização, `201` com `Location` na criação e `204` na exclusão. A API usa `400` para entrada inválida, `404` para registro inexistente e `409` para conflitos de integridade ou de negócio. Respostas de validação indicam os campos com erro.

### Exemplo de vínculo

~~~http
POST /api/atendidos
Content-Type: application/json

{"nome":"Duda Demo","dataNascimento":"2017-05-20","responsavelId":1}
~~~

Resposta esperada no banco recém-iniciado:

~~~json
{
  "id": 4,
  "nome": "Duda Demo",
  "dataNascimento": "2017-05-20",
  "responsavel": {"id": 1, "nome": "Responsável Demo"}
}
~~~

Para criar uma ficha desse cadastro:

~~~http
POST /api/fichas-socioeconomicas
Content-Type: application/json

{"atendidoId":4,"rendaFamiliar":1200.00,"pessoasNoDomicilio":3}
~~~

A resposta `201` inclui `atendido: {"id":4,"nome":"Duda Demo"}` e `rendaPorPessoa: 400.00`. A consulta da ficha pelo atendido deve recuperar o mesmo registro.

## Demonstração

- [Apresentação em PowerPoint](apresentacao/Seminario_JPA_Crianca_Feliz.pptx) com notas para a dupla.
- [Roteiro](demo/ROTEIRO.md) com divisão de fala e operações dos dois projetos.
- [Requisições HTTP](demo/requests.http) para IntelliJ, VS Code REST Client ou tradução direta para Postman/Insomnia.
- `pwsh -File ./demo/demonstrar.ps1` executa o CRUD das três entidades, valida os vínculos e verifica respostas de erro.
- [Exercício Categoria e Curso](https://github.com/leonardoMartinsB/seminario-jpa-crianca-feliz/blob/exercicio-categoria-curso/EXERCICIO.md) na branch dedicada ao projeto didático do professor.

Aula e enunciado: [Aula 01](https://glaucotodesco.notion.site/Aula-01-Relacionamentos-JPA-3fe5d52a81f58098b8a3ee702e13ad96) e [Projeto final](https://glaucotodesco.notion.site/Projeto-Final-Semin-rio-JPA-e-Projeto-Integrador-3fe5d52a81f581a296b0f262cecf8b7e).
