import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter; // import pra poder escrever arquivo
import java.io.IOException;
import java.io.FileOutpuStream;
import java.io.DataOutputStream;

public class Main {
    public static void main(String[] args) {
        // le o arquivo de entrada ao inves de usar string fixa
        String textoTeste = lerArquivo("entrada.txt");

        // calcula a frequencia de cada caractere
        int[] resultado = contarFrequencias(textoTeste);

        // imprime so os caracteres que realmente aparecem
        System.out.println("ETAPA 1: Tabela de Frequencia de Caracteres");
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] > 0) {
                System.out.println("Caractere '" + (char)i + "' (ASCII: " + i + "): " + resultado[i]);
            }
        }

        // constroi a arvore de huffman usando o heap
        No raiz = construirArvore(resultado);
        System.out.println("\nETAPA 3: Arvore de Huffman construida");

        // cria tabela onde vai guardar os codigos de cada caractere
        String[] tabela = new String[256];
        gerarCodigos(raiz, "", tabela);

        // imprime os codigos gerados
        System.out.println("\nETAPA 4: Tabela de Codigos");
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
                System.out.println("Caractere '" + (char)i + "': " + tabela[i]);
            }
        }

        // transforma o texto original em codigo binario
        String textoCodificado = codificar(textoTeste, tabela);

        System.out.println("\nETAPA 5: Texto Codificado");
        System.out.println(textoCodificado);

        // salva o resultado comprimido em arquivo
        salvarArquivo("saida.huff", resultado, textoCodificado);

        // calcula tamanho original (8 bits por caractere)
        int tamanhoOriginal = textoTeste.length() * 8;

        // tamanho depois de comprimido (ja em bits)
        int tamanhoComprimido = textoCodificado.length();

        // calcula a taxa de compressao em porcentagem
        double taxa = (1 - (double)tamanhoComprimido / tamanhoOriginal) * 100;

        System.out.println("\nETAPA 5: Resumo da Compressao");
        System.out.println("Tamanho original....: " + tamanhoOriginal + " bits");
        System.out.println("Tamanho comprimido..: " + tamanhoComprimido + " bits");
        System.out.printf("Taxa de compressao..: %.2f%%\n", taxa);

        // decodifica o texto usando a arvore pra testar se volta ao original
        String textoDecodificado = decodificar(textoCodificado, raiz);

        System.out.println("\nETAPA 6: Texto Decodificado");
        System.out.println(textoDecodificado);
    }

    public static int[] contarFrequencias(String texto){

        // vetor que guarda quantas vezes cada caractere aparece
        int[] frequencias = new int[256];

        // percorre o texto e soma +1 pra cada caractere encontrado
        for(int i = 0; i < texto.length(); i++){
            char caractere = texto.charAt(i);
            frequencias[caractere]++;
        }
        return frequencias;
    }

    public static No construirArvore(int[] frequencias) {
        // cria o heap (fila de prioridade)
        MinHeap heap = new MinHeap();

        // cria um no pra cada caractere que apareceu
        for (int i = 0; i < frequencias.length; i++) {
            if (frequencias[i] > 0) {
                heap.inserir(new No((char) i, frequencias[i]));
            }
        }

        // vai juntando os dois menores ate sobrar so a raiz
        while (heap.tamanho() > 1) {
            No noEsquerda = heap.remover();
            No noDireita = heap.remover();

            // cria um no pai somando as frequencias
            No pai = new No('\0', noEsquerda.frequencia + noDireita.frequencia);
            pai.esquerda = noEsquerda;
            pai.direita = noDireita;

            // coloca de volta no heap
            heap.inserir(pai);
        }

        // retorna a raiz final da arvore
        return heap.remover();
    }

    public static void gerarCodigos(No raiz, String codigo, String[] tabela) {
        if (raiz == null) return;

        // se for folha, salva o codigo do caractere
        if (raiz.esquerda == null && raiz.direita == null) {
            tabela[raiz.caractere] = codigo;
            return;
        }

        // esquerda = 0
        gerarCodigos(raiz.esquerda, codigo + "0", tabela);

        // direita = 1
        gerarCodigos(raiz.direita, codigo + "1", tabela);
    }

    public static String codificar(String texto, String[] tabela) {
        // string que vai guardar o resultado final
        String resultado = "";

        // substitui cada caractere pelo codigo dele
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            resultado += tabela[c];
        }

        return resultado;
    }

    public static String lerArquivo(String caminho) {
        // string builder pra ir montando o conteudo
        StringBuilder conteudo = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(caminho));
            String linha;

            // le linha por linha do arquivo
            while ((linha = br.readLine()) != null) {
                conteudo.append(linha);
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo");
        }

        return conteudo.toString();
    }

    public static void salvarArquivo(String caminho, int[] frequencia, String textoCodificado) {
        try(DataOutputStream dos = new DataOutputStream(new FileOutpuStream(caminho))) {
            for(int i = 0; i < 256; i++){
                dos.writeInt(frequencias[i]);
            }

            int buffer = 0;
            int contBits = 0;

            for(int i = 0; i < textoCodificado.length; i++){
                buffer <<= 1;
                if(textoCodificado.charAt(i) == '1');{
                    buffer |= 1;
                }
                contBits ++;

                if(contBits == 8){
                    dos.writeByte(buffer);
                    buffer = 0;
                    contBits = 0;
                }
            }

            if(contBits > 0){
                buffer <<= (8 - contBits);
                dos.writeByte(buffer);
            }

            System.out.println("Arquivo salvo com sucesso", + caminho);
        }

        catch (IOException){
            System.ou.println("Erro ao salvar arquivo binário", + e.getMessage());
        }
    }

    public static String decodificar(String codigo, No raiz) {
        String resultado = "";
        No atual = raiz;

        // percorre os bits um por um
        for (int i = 0; i < codigo.length(); i++) {
            char bit = codigo.charAt(i);

            // vai pra esquerda ou direita na arvore
            if (bit == '0') {
                atual = atual.esquerda;
            } else {
                atual = atual.direita;
            }

            // se chegou numa folha, encontrou um caractere
            if (atual.esquerda == null && atual.direita == null) {
                resultado += atual.caractere;
                atual = raiz; // volta pra raiz pra continuar
            }
        }

        return resultado;
    }
}