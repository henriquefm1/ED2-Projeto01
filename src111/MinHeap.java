import java.util.ArrayList;

public class MinHeap {
    private ArrayList<No> heap;

    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    public int tamanho() {
        return heap.size();
    }

    public void inserir(No novoNo) {
        heap.add(novoNo);
        int indiceAtual = heap.size() - 1;

        while (indiceAtual > 0) {
            int indicePai = (indiceAtual - 1) / 2;

            No noAtual = heap.get(indiceAtual);
            No noPai = heap.get(indicePai);

            if (noAtual.compareTo(noPai) < 0) {
                heap.set(indiceAtual, noPai);
                heap.set(indicePai, noAtual);
                indiceAtual = indicePai;
            } else {
                break;
            }
        }
    }

    public No remover() {
        if (heap.isEmpty()) {
            return null;
        }

        No menorNo = heap.get(0);
        No ultimoNo = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, ultimoNo);
            int indiceAtual = 0;

            while (true) {
                int filhoEsq = 2 * indiceAtual + 1;
                int filhoDir = 2 * indiceAtual + 2;
                int menorIndice = indiceAtual;

                // SUBSTITUÍDO: uso do compareTo em vez de apenas .frequencia
                if (filhoEsq < heap.size() && heap.get(filhoEsq).compareTo(heap.get(menorIndice)) < 0) {
                    menorIndice = filhoEsq;
                }

                if (filhoDir < heap.size() && heap.get(filhoDir).compareTo(heap.get(menorIndice)) < 0) {
                    menorIndice = filhoDir;
                }

                if (menorIndice != indiceAtual) {
                    No temp = heap.get(indiceAtual);
                    heap.set(indiceAtual, heap.get(menorIndice));
                    heap.set(menorIndice, temp);
                    indiceAtual = menorIndice;
                } else {
                    break;
                }
            }
        }

        return menorNo;
    }

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
