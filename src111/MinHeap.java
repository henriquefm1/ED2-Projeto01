//Beatriz Nóbrega RA: 10435789 
//Henrique Ferreira RA: 10439797 
//Matheus Guion RA: 10437693 

import java.util.ArrayList;

public class MinHeap {
    private ArrayList<No> heap;
    //o ArrayList é usado para representar a árvore binária do Min-Heap em um vetor
    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    public int tamanho() {
        return heap.size();
    }

    //método para inserir um novo caractere e garantir que o menor elemento fique sempre na raiz (indice 0)
    public void inserir(No novoNo) {
        //adiciona novo nó no final do vetor
        heap.add(novoNo);
        int indiceAtual = heap.size() - 1;

        //faz o nó "subir" ate encontrar sua posição correta na arvore 
        while (indiceAtual > 0) {
            //calculo para encontrar a posição no nó pai
            int indicePai = (indiceAtual - 1) / 2;

            No noAtual = heap.get(indiceAtual);
            No noPai = heap.get(indicePai);

            //se nó é menor que pai, troca de lugar
            if (noAtual.compareTo(noPai) < 0) {
                heap.set(indiceAtual, noPai);
                heap.set(indicePai, noAtual);

                //atualiza o indice para continuar verificando e subindo na proxima lida do while pelo programa
                indiceAtual = indicePai;
            } else {
                //para se nó é menor que o pai
                break;
            }
        }
    }

    //método para remover e retornar o nó com a menor frequência (a raiz do Heap) para montar a Árvore de Huffman
    public No remover() {
        //caso base: se o heap estiver vazio, não remove
        if (heap.isEmpty()) {
            return null;
        }

        //guarda o menor nó (que sempre está no topo, índice 0) para retornar no final
        No menorNo = heap.get(0);
        
        //retira o último nó do final da lista
        No ultimoNo = heap.remove(heap.size() - 1);

        //se ainda sobrarem elementos no Heap após a remoção
        if (!heap.isEmpty()) {
            //coloca aquele último nó no topo (no buraco deixado pela remoção)
            heap.set(0, ultimoNo);
            int indiceAtual = 0;

            //faz nó "descer" comparando-o com seus filhos
            while (true) {
                //calculos matemáticos padrão de Heaps para achar os filhos em um vetor
                int filhoEsq = 2 * indiceAtual + 1;
                int filhoDir = 2 * indiceAtual + 2;
                int menorIndice = indiceAtual; //começa assumindo que o pai já é o menor

                //verifica se o filho da esquerda existe e se é MENOR que o pai
                if (filhoEsq < heap.size() && heap.get(filhoEsq).compareTo(heap.get(menorIndice)) < 0) {
                    menorIndice = filhoEsq;
                }

                //verifica se o filho da direita existe e se é MENOR ainda que o vencedor da etapa anterior
                if (filhoDir < heap.size() && heap.get(filhoDir).compareTo(heap.get(menorIndice)) < 0) {
                    menorIndice = filhoDir;
                }

                //se descobrimos que um dos filhos é menor que o pai, troca
                if (menorIndice != indiceAtual) {
                    No temp = heap.get(indiceAtual);
                    heap.set(indiceAtual, heap.get(menorIndice));
                    heap.set(menorIndice, temp);
                    
                    //prepara o índice para descer mais um nível e analisar os próximos filhos
                    indiceAtual = menorIndice;
                } else {
                    //se nenhum filho for menor, o nó encontrou o seu lugar e a árvore está correta
                    break;
                }
            }
        }

        //retorna o nó com a menor frequência que foi guardado no passo 1
        return menorNo;
    }

    //método exigido pelo PDF (Seção 5) para mostrar a estrutura intermediária no console
    public void imprimir() {
        System.out.print("[");
        for (int i = 0; i < heap.size(); i++) {
            System.out.print("No('" + heap.get(i).caractere + "', " + heap.get(i).frequencia + ")");
            
            //adiciona a vírgula para separar os itens, exceto no último elemento
            if (i < heap.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
