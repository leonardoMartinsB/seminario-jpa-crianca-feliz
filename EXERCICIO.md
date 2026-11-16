# Categoria e Curso

Leonardo Martins Brasilio e Pedro Henrique Menezes Bernardo · Grupo Criança Feliz

Exercício de fixação do professor Glauco Todesco. Esta branch deriva do [projeto da aula](https://github.com/glaucotodesco/Java-Spring-JPA-Relacionamentos) no commit `8490cf7`. O [backend do Projeto Integrador](https://github.com/CriancaFeliz/sistema-crianca-feliz/tree/codex/seminario-jpa/backend-jpa) fica em uma branch própria.

## Modelagem

1. `Curso.categoria` usa `@ManyToOne` com `@JoinColumn(name = "categoria_id")` e mapeia o lado proprietário.
2. `Categoria.cursos` usa `@OneToMany(mappedBy = "categoria")` para navegar no sentido inverso.
3. A tabela `CURSOS` guarda a FK para `CATEGORIAS.ID`.
4. A coluna se chama `CATEGORIA_ID` e é obrigatória.
5. Cada curso aponta para uma única categoria. Uma FK direta registra a relação 1:N.

~~~mermaid
erDiagram
    CATEGORIAS ||--o{ CURSOS : classifica
    CATEGORIAS {
        bigint id PK
        varchar nome
    }
    CURSOS {
        bigint id PK
        bigint categoria_id FK
    }
~~~

## Demonstração

O arquivo [exercicio.http](exercicio.http) reúne as requisições.

1. Com Java 21 e Maven 3.9+, execute `mvn spring-boot:run`.
2. Em `http://127.0.0.1:8080`, faça `POST /categorias` com `{"nome":"Backend"}` e guarde o ID devolvido.
3. Execute `GET /categorias` e `GET /categorias/{id}`. Em uma execução nova, a categoria recém-criada normalmente recebe ID 2, pois o seed já cadastrou Geral.
4. Execute `PUT /cursos/1/categoria/{id}`. Consulte `GET /cursos/1`. A resposta contém `"categoria":{"id":2,"nome":"Backend"}` se o ID devolvido no passo anterior foi 2.
5. Abra `http://localhost:8080/h2-console`. Use a URL `jdbc:h2:mem:relacionamentos`, usuário `sa` e senha vazia. Execute:

~~~sql
SELECT * FROM CATEGORIAS;
SELECT * FROM CURSOS;
~~~

A linha do curso 1 deve apontar para o ID da categoria Backend. Se informar uma categoria inexistente, a API devolve `404` e conserva a associação anterior.

O CRUD original de curso também aceita `categoriaId` no JSON. Na criação, omitir o ID usa a categoria Geral. Na atualização, omitir o ID conserva a categoria atual.
