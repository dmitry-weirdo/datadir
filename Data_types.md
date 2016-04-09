| **Фундаментальный тип** | **Мнемоника** | **Java класс** | **Тип базы данных (на примере Firebird)** | **Тип базы данных (на примере MS SQL Server)** |
|:------------------------|:--------------|:---------------|:------------------------------------------|:-----------------------------------------------|
| строка                  | STRING        | String         | varchar                                   | varchar                                        |
| целое число             | LONG          | Long           | int64                                     | bigint                                         |
| вещественное число      | DOUBLE        | Double         | double                                    | real                                           |
| булев тип (истина/ложь) | BOOLEAN       | Boolean        | smallint                                  | tinyint                                        |
| символ                  | CHARACTER     | Character      | char                                      | char                                           |
| файл                    | FILE          | byte**[**] + метаданные | ссылка на объект Attachment               | ссылка на объект Attachment                    |
| набор байт неограниченного размера (или строка неограниченной длины) | BLOB          | byte**[**]     | blob                                      | image                                          |
| дата                    | DATE          | Date           | timestamp                                 |  date                                          |
| дата и время            | DATETIME      | Date           | timestamp                                 | datetime                                       |
| перечислимый тип/справочник/ссылка на другую сущность | LINK          | enum/Object    | foreign key                               | foreign key                                    |

См. http://www.firebirdsql.org/manual/migration-mssql-data-types.html