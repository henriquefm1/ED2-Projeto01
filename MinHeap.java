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
    public int inserir(No no){
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
