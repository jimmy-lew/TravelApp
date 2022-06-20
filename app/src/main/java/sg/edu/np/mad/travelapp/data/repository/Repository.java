package sg.edu.np.mad.travelapp.data.repository;

public interface Repository {
    interface OnComplete<T> {
        void execute(T data);
    }
}
