# Roteiro da apresentação

## Antes da aula

1. Iniciar o projeto de relacionamentos do professor e o módulo do PI em terminais separados. O primeiro usa a porta 8080 e o segundo 8081.
2. Abrir `requests.http` em um cliente HTTP compatível ou copiar as requisições para Postman. Abrir o console H2 dos dois projetos com a respectiva URL JDBC.
3. Usar as notas dos slides. O professor define o tema do grupo. Os slides de comparação permitem escolher o percurso e os outros tópicos servem como apoio para perguntas.

## Seminário JPA (15 a 20 minutos)

| Tempo | Quem | Fala e demonstração |
|---|---|---|
| 0 a 2 min | Leonardo | Apresenta a regra de negócio e lê a cardinalidade nos dois sentidos. |
| 2 a 7 min | Leonardo | Mostra a entidade proprietária, a anotação e a FK ou a tabela de junção no H2. |
| 7 a 12 min | Pedro | Executa o endpoint, localiza os IDs e interpreta o JSON. |
| 12 a 16 min | Pedro | Reproduz um erro comum e explica a correção. Repete a consulta e compara banco e JSON. |
| 16 a 18 min | Ambos | Respondem sobre `mappedBy`, dados inválidos, transações e serialização. |

Percursos conforme o tema:

- **Um para um:** consulte `GET /alunos/1` e mostre `ALUNOS.PERFIL_ID`. Com perfil já usado, tente vinculá-lo a outro aluno e explique a unicidade. Confira a resposta no cliente antes de projetar.
- **Muitos para um e um para muitos:** consulte `GET /instrutores/1/cursos`, troque o instrutor de um curso com `PUT /cursos/1/instrutor/2` e confira `CURSOS.INSTRUTOR_ID`. Explique por que alterar apenas a lista inversa não atualiza a FK.
- **Muitos para muitos:** matricule com `POST /alunos/1/cursos/3`, consulte os cursos desse aluno e mostre a nova linha em `ALUNO_CURSO`. Remova o vínculo com `DELETE /alunos/1/cursos/3` e confira de novo.
- **Integridade e H2:** execute a associação Categoria–Curso ou repita a criação de ficha do PI. Mostre por que uma FK inexistente e uma FK duplicada são situações diferentes.
- **API REST:** compare o ID enviado com o resumo recebido, mostre o DTO e um retorno de validação. Explique como um DTO sem referências reversas evita serialização circular.

## Demonstração do Criança Feliz

1. **Leonardo** mostra a estrutura Controller, Service, Repository e Mapper. Consulta `GET /api/responsaveis/1/atendidos` e mostra as duas linhas do mesmo responsável no H2.
2. **Pedro** cria um atendido com `responsavelId: 1`, anota o ID recebido e cria sua ficha. Consulta `GET /api/atendidos/{id}/ficha-socioeconomica` e mostra a FK e a renda por pessoa.
3. **Pedro** repete a criação para receber `409` e envia zero moradores para receber `400`. **Leonardo** mostra no H2 a restrição única em `ATENDIDO_ID`.
4. Para conferir o CRUD inteiro, rodar `demo/demonstrar.ps1` no PowerShell 7. A demonstração manual pode ficar visível no cliente HTTP enquanto o outro integrante mostra as classes.

### Perguntas para a dupla

- **Por que a FK fica no atendido?** Porque cada atendido guarda no máximo um responsável, que pode atender vários cadastros.
- **A FK garante uma ficha só?** A FK garante que o atendido existe. A restrição UNIQUE impede a segunda ficha.
- **Por que usar DTOs?** O contrato HTTP expõe campos úteis e resumos. A representação não precisa seguir toda a navegação entre entidades.
- **Qual a diferença entre 400, 404 e 409?** Entrada inválida, cadastro ausente e operação incompatível com o estado atual, respectivamente.
- **O que reiniciar o H2 faz?** O processo descarta o banco em memória. A nova inicialização recria as tabelas e os dados fictícios.
