import java.util.ArrayList;

public class MinHeap{
    private ArrayList<No> heap;

    public MinHeap(){
        this.heap = new ArrayList<>();
    }

    public int tamanho(){
        return heap.size();
    }

    //inserir frequencia na arvore
    public void inserir(No novoNo){
        //adiciona novo nó no final da lista de arrays
        heap.add(novoNo);

        //pega o indice onde entrou
        int indiceAtual = heap.size() - 1;
        
        //faz o nó achar a posição correta
        while(indiceAtual > 0){
            //calcula o pai do nó
            int indicePai = (indiceAtual - 1) / 2;

            No noAtual = heap.get(indiceAtual);
            No noPai = heap.get(indicePai);

            //verifica as frequencias, se for menor -> troca de lugar, se não, break
            if(noAtual.frequencia < noPai.frequencia){
                heap.set(indiceAtual, noPai);
                heap.set(indicePai, noAtual);
                indiceAtual = indicePai;
            }else{
                break;
            }
        }
    }

    public No remover() {
        if (heap.isEmpty()) {
            return null; //Caso base, se estiver vazio, não faz nada
        }

        //guarda o menor no
        No menorNo = heap.get(0);

        //remove o ultimo no da lista
        No ultimoNo = heap.remove(heap.size() - 1);

        //se o heap não ficou vazio depois de remover, coloca o ultimo no topo e desce
        if (!heap.isEmpty()) {
            heap.set(0, ultimoNo);
            
            //faz o nó descer e compara com os filhos
            int indiceAtual = 0;
            
            while (true) {
                int filhoEsq = 2 * indiceAtual + 1;
                int filhoDir = 2 * indiceAtual + 2;
                int menorIndice = indiceAtual;

                //verifica se o filho da esquerda existe e é menor
                if (filhoEsq < heap.size() && heap.get(filhoEsq).frequencia < heap.get(menorIndice).frequencia) {
                    menorIndice = filhoEsq;
                }

                //verifica se o filho da direita existe e é menor
                if (filhoDir < heap.size() && heap.get(filhoDir).frequencia < heap.get(menorIndice).frequencia) {
                    menorIndice = filhoDir;
                }

                //se encontra um filho menor, troca de lugar
                if (menorIndice != indiceAtual) {
                    No temp = heap.get(indiceAtual);
                    heap.set(indiceAtual, heap.get(menorIndice));
                    heap.set(menorIndice, temp);
                    //prepara para a proxima analise
                    indiceAtual = menorIndice;
                } else { //se nenhum filho for menor, break pois ja esta no lugar correto
                    break;
                }
            }
        }

        return menorNo;
    }
    //função pra imprimir
    public void imprimir() {
        System.out.print("[");
        for (int i = 0; i < heap.size(); i++) {
            System.out.print("No('" + heap.get(i).caractere + "', " + heap.get(i).frequencia + ")");
            if (i < heap.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
