package avro.protocol;

import cn.gao.User;
import org.apache.avro.AvroRemoteException;

/**
 * Created by gao on 2016/12/2.
 */
public class AddServiceImpl implements AddService{

    @Override
    public int add(int x, int y) throws AvroRemoteException {
        return x+y;
    }

    @Override
    public Void sendUser(User user) throws AvroRemoteException {
        System.out.println(user);
        return null;
    }
}
