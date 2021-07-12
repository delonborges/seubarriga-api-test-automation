# Seu Barriga / Curso de testes automatizados
 
## Projeto de testes automatizados usando a API Seu Barriga

### Ambiente

- [JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html "JDK")
    - (Versão utilizada: 11.0.11)
- [Maven](https://maven.apache.org/download.cgi "Maven")
    - (Versão utilizada: 3.8.1)

### Configuração

Clone o repositório:
```
git clone git@github.com:delonborges/seubarriga-api-test-automation.git
```
Acesse a pasta do projeto:
```
cd seubarriga-api-test-automation
```
Efetue a instalação das dependências (os testes serão iniciados também):
```
mvn install
```
Ou, efetue a instalação das depêndencias, sem a execução dos testes:
```
mvn install -DskipTests
```


### Execução
Inicie a execução dos testes:
```
mvn test
```
Ou, execute o comando abaixo para executar a suíte de testes `TestsSuite`, especificamente:
```
mvn -Dtest=TestsSuite test 
```