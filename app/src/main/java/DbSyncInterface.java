/**
 * Created by Mohsin on 11/9/2017.
 */

public interface DbSyncInterface<T> {
    void callback(T data);
}
