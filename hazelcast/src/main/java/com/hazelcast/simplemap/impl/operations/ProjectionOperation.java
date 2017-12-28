package com.hazelcast.simplemap.impl.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.simplemap.impl.SimpleMapDataSerializerHook;
import com.hazelcast.simplemap.impl.SimpleRecordStore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectionOperation  extends SimpleMapOperation {

    private Map<String, Object> bindings;
    private String compiledQueryUuid;

    public ProjectionOperation() {
    }

    public ProjectionOperation(String name, String compiledQueryUuid, Map<String, Object> bindings) {
        super(name);
        this.compiledQueryUuid = compiledQueryUuid;
        this.bindings = bindings;
    }

    @Override
    public void run() throws Exception {
        SimpleRecordStore recordStore = container.getRecordStore(getPartitionId());
        recordStore.projection(compiledQueryUuid, bindings);
    }

    @Override
    public int getId() {
        return SimpleMapDataSerializerHook.QUERY;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);

        out.writeUTF(compiledQueryUuid);
        out.writeInt(bindings.size());
        for(Map.Entry<String,Object> entry: bindings.entrySet()){
            out.writeUTF(entry.getKey());
            out.writeObject(entry.getValue());
        }
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);

        compiledQueryUuid = in.readUTF();
        int size = in.readInt();
        bindings = new HashMap<String, Object>();
        for(int k=0;k<size;k++){
            bindings.put(in.readUTF(), in.readObject());
        }
    }
}