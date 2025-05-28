# Jogo da Memoria
## Sobre
Este projeto é um jogo da memória desenvolvido em Java, com o objetivo de praticar boas práticas de desenvolvimento de software.  
O projeto foi dividido em 3 releases (0.1v, 0.2v e 1.0v), cada uma adicionando novas funcionalidades até a versão final.

## Pré-requisitos
- Java JDK 11 ou superior.
- IDE (opcional, mas recomendado para melhor experiência).

## Como Executar

### Via Terminal

1. Clone o repositório:
```bash
   git clone https://github.com/seu-usuario/jogo-da-memoria.git
```

2. Compile e execute:
```bash
   cd jogoMemoria
   javac src/main/java/Main.java
   java -cp src/main/java Main
```

### Via IDE(Intellij/Eclipse)

1. Importe o projeto como um projeto Java existente.
2. Execute a classe **Main** em **src/main/java**.

## Funcionalidades da Versão Atual

### 0.1v
– Implementação da interface gráfica com Swing, exibindo cartas viradas para baixo.

– Lógica para virar as cartas e exibir o valor delas.

– Contabilização de tentativas.
### 0.2v
– Implementação da lógica para comparar as cartas viradas e verificar se são iguais.

– Condição de fim de jogo, exibindo a pontuação (número de tentativas) final.

– Botão para reiniciar o jogo.

### 1.0v (atual)
– Ao final de cada partida, o jogo deve solicitar o nome do jogador junto com a exibição do seu resultado.

– O placar (número de tentativas e nome do jogador) deve ser salvo em um arquivo JSON, YAML ou CSV.

– Implementar uma tela adicional com os 10 melhores placares, ordenados do menor número de tentativas para o maior.

## Estrutura do Projeto

**scr/main/java**: Código fonte principal.

**scr/test/java**: Testes unitários JUnit.

**/docs**: Documentação html javaDoc.

**/placares**: Pasta onde são salvos arquivos JSON com números de tentativas de cada jogador.

**/release**: Pasta com versões todas as versões do jogo da memória.
