package com.geo.rcs.modules.engine.handler;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.KryoObjectInput;
import com.esotericsoftware.kryo.io.KryoObjectOutput;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.KieBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wp
 * @date Created in 16:13 2019/2/27
 */
public class SerializeUtil {

    private static Map<String,KnowledgeBaseImpl> map = new ConcurrentHashMap<>(50);
    private static Map<String,String> ruleContentMap = new ConcurrentHashMap<>(50);

    private static Pool<Kryo> mKryoPool = new Pool<Kryo>(true, false, 8) {
        @Override
        protected Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.setReferences(false);
            // Configure the Kryo instance.
            return kryo;
        }
    };

    private static Pool<Output> mOutputPool = new Pool<Output>(true, false, 16) {
        @Override
        protected Output create() {
            return new Output(1024, -1);
        }
    };

    private static Pool<Input> mInputPool = new Pool<Input>(true, false, 16) {
        @Override
        protected Input create() {
            return new Input(1024);
        }
    };

    public SerializeUtil() {
        // TODO Auto-generated constructor stub
    }

    public static byte[] serialize(KnowledgeBaseImpl object) throws IOException {
        Kryo lvKryo = mKryoPool.obtain();
        Output lvOutput = mOutputPool.obtain();
        try (KryoObjectOutput objectOutput = new KryoObjectOutput(lvKryo, lvOutput)) {
            object.writeExternal(objectOutput);
            return lvOutput.getBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static KnowledgeBaseImpl unserialize(String rulesKey, byte[] pvBytes,String ruleContent) {
        Kryo lvKryo = mKryoPool.obtain();
        Input lvInput = mInputPool.obtain();
        lvInput.setBuffer(pvBytes);
        KryoObjectInput objectInput = new KryoObjectInput(lvKryo,lvInput);
        try {
            KnowledgeBaseImpl obj = new KnowledgeBaseImpl();
            obj.readExternal(objectInput);
            if(map.keySet().size() == 50){
                map = new HashMap<>(50);
            }
            map.put(rulesKey,obj);
            ruleContentMap.put(rulesKey,ruleContent);
            objectInput.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                mKryoPool.free(lvKryo);
                mInputPool.free(lvInput);
            } catch (Exception ignored){

            }
        }
        return null;
    }

    public static KieBase getKieBase(String rulesKey){
        return map.get(rulesKey);
    }

    public static boolean isContains(String rulesKey){
        return map.keySet().contains(rulesKey);
    }

    public static void updateCache(String rulesKey,KnowledgeBaseImpl knowledgeBase,String ruleContent){
        map.put(rulesKey,knowledgeBase);
        ruleContentMap.put(rulesKey,ruleContent);
    }

    public static boolean compareContent(String rulesKey, String ruleContent) {
        return ruleContentMap.get(rulesKey)!=null&&ruleContent.equals(ruleContentMap.get(rulesKey));
    }
}
