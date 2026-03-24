public class Main {
    public static void main(String[] args) {
        //testa a função com a palavra do exemplo no pdf
        String textoTeste = "BANANA";
        int[] resultado = contarFrequencias(textoTeste);

        //imprime apenas os caracteres que apareceram se a frequencia for maior que 0
        System.out.println("ETAPA 1: Tabela de Frequencia de Caracteres");
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] > 0) {
                System.out.println("Caractere '" + (char)i + "' (ASCII: " + i + "): " + resultado[i]);
            }
        }

        //constroi a arvore de huffman
        No raiz = construirArvore(resultado);
        System.out.println("\nETAPA 3: Arvore de Huffman construida");

        //gera tabela de codigos
        String[] tabela = new String[256];
        gerarCodigos(raiz, "", tabela);

        //imprime tabela de codigos
        System.out.println("\nETAPA 4: Tabela de Codigos");
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
                System.out.println("Caractere '" + (char)i + "': " + tabela[i]);
            }
        }

        //codifica o texto
        String textoCodificado = codificar(textoTeste, tabela);

        System.out.println("\nETAPA 5: Texto Codificado");
        System.out.println(textoCodificado);

        //resumo da compressao
        int tamanhoOriginal = textoTeste.length() * 8;
        int tamanhoComprimido = textoCodificado.length();

        double taxa = (1 - (double)tamanhoComprimido / tamanhoOriginal) * 100;

        System.out.println("\nETAPA 5: Resumo da Compressao");
        System.out.println("Tamanho original....: " + tamanhoOriginal + " bits");
        System.out.println("Tamanho comprimido..: " + tamanhoComprimido + " bits");
        System.out.printf("Taxa de compressao..: %.2f%%\n", taxa);
    }

    public static int[] contarFrequencias(String texto){

        //cria um vetor onde todas as 256 posições começam com 0
        int[] frequencias = new int[256];

        //passa por cada letra do texto
        for(int i = 0; i < texto.length(); i++){
            char caractere = texto.charAt(i);

            //o caractere vira o indice do vetor, soma +1 naquela posição
            frequencias[caractere]++;
        }
        return frequencias;
    }

    public static No construirArvore(int[] frequencias) {
        MinHeap heap = new MinHeap(); // cria o heap

        // cria nós folha para cada caractere existente
        for (int i = 0; i < frequencias.length; i++) {
            if (frequencias[i] > 0) {
                heap.inserir(new No((char) i, frequencias[i]));
            }
        }

        // monta a árvore juntando os menores
        while (heap.tamanho() > 1) {
            No noEsquerda = heap.remover(); // menor
            No noDireita = heap.remover();  // segundo menor

            // cria nó pai somando frequências
            No pai = new No('\0', noEsquerda.frequencia + noDireita.frequencia);
            pai.esquerda = noEsquerda;
            pai.direita = noDireita;

            heap.inserir(pai); // volta pro heap
        }

        return heap.remover(); // raiz da árvore
    }

    public static void gerarCodigos(No raiz, String codigo, String[] tabela) {
        if (raiz == null) return;

        // se for folha, salva o código
        if (raiz.esquerda == null && raiz.direita == null) {
            tabela[raiz.caractere] = codigo;
            return;
        }

        // percorre esquerda (0)
        gerarCodigos(raiz.esquerda, codigo + "0", tabela);

        // percorre direita (1)
        gerarCodigos(raiz.direita, codigo + "1", tabela);
    }

    public static String codificar(String texto, String[] tabela) {
        String resultado = "";

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            resultado += tabela[c];
        }

        return resultado;
    }
}