import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.EOFException;

public class Main {
    public static void main(String[] args) {
        //verificação de quantidade mínima de argumentos
        if (args.length < 3) {
            System.out.println("Uso incorreto! Utilize:");
            System.out.println("Para comprimir: java -jar huffman.jar c <arquivo_original> <arquivo_comprimido>");
            System.out.println("Para descomprimir: java -jar huffman.jar d <arquivo_comprimido> <arquivo_restaurado>");
            return;
        }

        //lê os argumentos
        String comando = args[0];
        String arquivoEntrada = args[1];
        String arquivoSaida = args[2];

        if (comando.equals("c")) {
            System.out.println("Iniciando Compressao...\n");
            
            // Inicia o cronômetro para o relatório
            long tempoInicio = System.nanoTime();
            
            String textoOriginal = lerArquivo(arquivoEntrada);
            if (textoOriginal.isEmpty()) {
                System.out.println("Arquivo de entrada vazio ou não encontrado.");
                return;
            }

            int[] resultado = contarFrequencias(textoOriginal);

            //impressão da Tabela de Frequência
            System.out.println("ETAPA 1: Tabela de Frequencia de Caracteres");
            for (int i = 0; i < resultado.length; i++) {
                if (resultado[i] > 0) {
                    System.out.println("Caractere '" + (char)i + "' (ASCII: " + i + "): " + resultado[i]);
                }
            }

            //impressão do Min-Heap é chamada dentro da função construirArvore
            No raiz = construirArvore(resultado);
            System.out.println("\nETAPA 3: Arvore de Huffman construida com sucesso.");
            
            String[] tabela = new String[256];
            gerarCodigos(raiz, "", tabela);

            //impressão da Tabela de Códigos
            System.out.println("\nETAPA 4: Tabela de Codigos de Huffman");
            for (int i = 0; i < tabela.length; i++) {
                if (tabela[i] != null) {
                    System.out.println("Caractere '" + (char)i + "': " + tabela[i]);
                }
            }

            String textoCodificado = codificar(textoOriginal, tabela);
            
            //salva o arquivo em binário
            salvarArquivo(arquivoSaida, resultado, textoCodificado);

            //calcula o tempo de execução
            long tempoFim = System.nanoTime();
            long tempoExecucaoMs = (tempoFim - tempoInicio) / 1000000;

            int tamanhoOriginal = textoOriginal.length() * 8;
            int tamanhoComprimido = textoCodificado.length();
            double taxa = (1 - (double)tamanhoComprimido / tamanhoOriginal) * 100;

            //prints finais
            System.out.println("\nETAPA 5: Resumo da Compressao");
            System.out.println("Tamanho original......: " + tamanhoOriginal + " bits");
            System.out.println("Tamanho comprimido....: " + tamanhoComprimido + " bits");
            System.out.printf("Taxa de compressao....: %.2f%%\n", taxa);
            System.out.println("Tempo de execucao.....: " + tempoExecucaoMs + " ms"); // Dado extra para o relatório!

        } else if (comando.equals("d")) {
            System.out.println("Iniciando Descompressao...\n");
            
            long tempoInicio = System.nanoTime();
            descomprimirArquivo(arquivoEntrada, arquivoSaida);
            long tempoFim = System.nanoTime();
            long tempoExecucaoMs = (tempoFim - tempoInicio) / 1000000;
            
            System.out.println("Tempo de execucao da descompressao: " + tempoExecucaoMs + " ms");
            
        } else {
            System.out.println("Comando inválido. Use 'c' para comprimir ou 'd' para descomprimir.");
        }
    }

    //cria um vetor de 256, e lê a frequência de cada letra
    public static int[] contarFrequencias(String texto){
        int[] frequencias = new int[256];
        for(int i = 0; i < texto.length(); i++){
            frequencias[texto.charAt(i)]++;
        }
        return frequencias;
    }

    //constroi a árvore de huffman de cima para baixo, seguindo o MinHeap
    public static No construirArvore(int[] frequencias) {
        MinHeap heap = new MinHeap();
        
        //Cria uma folha para cada caractere que aparece pelo menos uma vez
        for (int i = 0; i < frequencias.length; i++) {
            if (frequencias[i] > 0) {
                heap.inserir(new No((char) i, frequencias[i]));
            }
        }
        
        //imprime o Heap Inicial antes de começar a combinar os nós
        System.out.println("\nETAPA 2: Min-Heap Inicial (Vetor)");
        heap.imprimir();

        //Combina os nós de menor frequêcia repetidamente até sobrar a raiz
        while (heap.tamanho() > 1) {
            No noEsquerda = heap.remover();
            No noDireita = heap.remover();

            //Cria um nó pai com a soma das frequências
            No pai = new No('\0', noEsquerda.frequencia + noDireita.frequencia);
            pai.esquerda = noEsquerda;
            pai.direita = noDireita;
            heap.inserir(pai);
        }
        //último nó é a raiz da árvore de huffman
        return heap.remover();
    }

    //Percorre a árvore recursivamente para dar os códigos binários
    public static void gerarCodigos(No raiz, String codigo, String[] tabela) {
        if (raiz == null) return;
        //Se for um nó folha, chegamos a um caractere. Salvamos o código
        if (raiz.esquerda == null && raiz.direita == null) {
            tabela[raiz.caractere] = codigo;
            return;
        }

        //Chamadas recursivas descendo a árvore
        gerarCodigos(raiz.esquerda, codigo + "0", tabela);
        gerarCodigos(raiz.direita, codigo + "1", tabela);
    }

    //Converte o texto original para um string de 0s e 1s
    public static String codificar(String texto, String[] tabela) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            resultado.append(tabela[texto.charAt(i)]);
        }
        return resultado.toString();
    }

    //Navega a árvore de Huffman seguindo os bits codificados, para achar seu significado original
    public static String decodificar(String codigo, No raiz) {
        StringBuilder resultado = new StringBuilder();
        No atual = raiz;
        for (int i = 0; i < codigo.length(); i++) {
            char bit = codigo.charAt(i);
            
            //Desce à esquerda se o bit for 0, ou direita se o bit for 1
            if (bit == '0') {
                atual = atual.esquerda;
            } else {
                atual = atual.direita;
            }

            //Se chegou um nó na folha, encontramos o caractere
            if (atual.esquerda == null && atual.direita == null) {
                resultado.append(atual.caractere);
                atual = raiz;//Volta à raiz para buscar o próximo caractere
            }
        }
        return resultado.toString();
    }

    //Lê o arquivo de texto inteiro em memória
    public static String lerArquivo(String caminho) {
        StringBuilder conteudo = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            int c;
            while ((c = br.read()) != -1) {
                conteudo.append((char) c);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
        return conteudo.toString();
    }

    //Salva informações necessárias para a descompressão
    public static void salvarArquivo(String caminho, int[] frequencias, String textoCodificado) {
        //Usa JavaOutputStream para escrever Java de forma binária
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(caminho))) {
            //Grava o cabeçalho - vetor de frequências
            for (int i = 0; i < 256; i++) {
                dos.writeInt(frequencias[i]);
            }
            
            //Grava a quantidade exata de bits úteis
            dos.writeInt(textoCodificado.length());

            //Agrupa os 0s e 1s em bits reais ou invés de Strings
            int buffer = 0;
            int contBits = 0;
            for (int i = 0; i < textoCodificado.length(); i++) {
                buffer <<= 1;//Empurra os bits do buffer, uma casa para a direita
                if (textoCodificado.charAt(i) == '1') {
                    buffer |= 1;//Se o bit atual for 1, liga ao último bit do buffer
                }
                contBits++;
                //Quando completar 8 bits, escreve o byte no arquivo e zera os contadores
                if (contBits == 8) {
                    dos.writeByte(buffer);
                    buffer = 0;
                    contBits = 0;
                }
            }
            //Caso bits que não completaram 8 bits, precisamos completar com 0s a direita
            if (contBits > 0) {
                buffer <<= (8 - contBits);
                dos.writeByte(buffer);
            }
            System.out.println("\n-> Arquivo comprimido salvo com sucesso: " + caminho);
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo binario: " + e.getMessage());
        }
    }

    //Lê o arquivo binário, recria a árvore, extrai os bits e restaura o arquivo original
    public static void descomprimirArquivo(String arquivoEntrada, String arquivoSaida) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(arquivoEntrada))) {
            //Lê o vetor de frequências
            int[] frequencias = new int[256];
            for (int i = 0; i < 256; i++) {
                frequencias[i] = dis.readInt();
            }

            //Lê o tamanho exato de bits originais comprimidos
            int tamanhoRealBits = dis.readInt();

            //Reconstrói a mesma árvore de Huffman usada no início
            No raiz = construirArvore(frequencias);

            StringBuilder bitsCodificados = new StringBuilder();
            //Lê todos os bits e converte de volta para string de 0s e 1s
            try {
                while (true) {//loop infinito q acabará por conta da exceção
                    byte b = dis.readByte();
                    //Extrai bit a bit do byte lido
                    for (int i = 7; i >= 0; i--) {
                        bitsCodificados.append((b >> i) & 1);
                    }
                }
            } catch (EOFException e) {
                //fim do arquivo
            }

            //Corta os bits extras de preenchimento
            String bitsValidos = bitsCodificados.substring(0, tamanhoRealBits);

            //Decodifica a string binária validada pela árvore
            String textoDecodificado = decodificar(bitsValidos, raiz);

            //Salva o texto restaurado em um arquivo normal
            try (FileWriter fw = new FileWriter(arquivoSaida)) {
                fw.write(textoDecodificado);
            }

            System.out.println("-> Arquivo descomprimido com sucesso: " + arquivoSaida);

        } catch (IOException e) {
            System.out.println("Erro na descompressao: " + e.getMessage());
        }
    }
}
