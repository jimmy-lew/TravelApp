package sg.edu.np.mad.travelapp.data.repository;

/**
 * Repository interface for async callback functions to act as completion listeners
 */
// TODO: Move into BusStopRepository.java
public interface IRepository {
    interface OnComplete<T> {
        void execute(T data);
    }
}
