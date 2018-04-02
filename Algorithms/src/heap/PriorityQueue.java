package heap;

import java.util.NoSuchElementException;

public interface PriorityQueue <T> {

    /**
     * Encola un elemento.
     *
     * @param elem     elemento a encolar.
     * @param priority nÃºmero mayor a cero que especifica la prioridad. Cuanto menor
     *                 es este nÃºmero mayor es la prioridad.
     * @throws IllegalStateException Si no hay lugar en la cola en el caso de colas con tamaÃ±o fijo.
     */
    public void enqueue (T elem, int priority);

    /**
     * Desencola un elemento.
     *
     * @throws NoSuchElementException Si la cola estÃ¡ vacÃ­a.
     */
    public T dequeue ();

    /**
     * Devuelve el tamaÃ±o de la cola.
     */
    public int size ();

    /**
     * Devuelve la prioridad de un elemento.
     *
     * @param elem elemento a consultar prioridad.
     * @return prioridad del elemento.
     * @throws NoSuchElementException Si no existe el elemento.
     */
    public int getPriority (T elem);

    /**
     * Devuelve la prioridad del siguiente elemento a desencolar.
     */
    public int minPriority ();

    /**
     * Decrece la prioridad de un elemento.
     *
     * @param elem     elemento a decrecer prioridad.
     * @param priority nueva prioridad menor a la prioridad actual.
     * @throws IllegalArgumentException si la nueva prioridad es mayor a la actual.
     */
    public void decreasePriority (T elem, int priority);

    /**
     * EvalÃºa si la cola estÃ¡ vacÃ­a o no.
     *
     * @return true si estÃ¡ vacÃ­a, false sino.
     */
    public boolean isEmpty ();
}