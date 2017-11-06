package io.servide.schematic.nbt;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

public class CompoundTagBuilder {

	public static CompoundTagBuilder create()
	{
		return new CompoundTagBuilder();
	}

    private final Map<String, Tag> entries;

    CompoundTagBuilder()
    {
        this.entries = new HashMap<>();
    }

    CompoundTagBuilder(Map<String, Tag> value)
    {
        checkNotNull(value);
        this.entries = value;
    }

    public CompoundTagBuilder put(String key, Tag value)
    {
        checkNotNull(key);
        checkNotNull(value);
	    this.entries.put(key, value);
        return this;
    }

    public CompoundTagBuilder putByteArray(String key, byte[] value)
    {
        return this.put(key, new ByteArrayTag(value));
    }

    public CompoundTagBuilder putByte(String key, byte value)
    {
        return this.put(key, new ByteTag(value));
    }

    public CompoundTagBuilder putDouble(String key, double value)
    {
        return this.put(key, new DoubleTag(value));
    }

    public CompoundTagBuilder putFloat(String key, float value)
    {
        return this.put(key, new FloatTag(value));
    }

    public CompoundTagBuilder putIntArray(String key, int[] value)
    {
        return this.put(key, new IntArrayTag(value));
    }

    public CompoundTagBuilder putInt(String key, int value)
    {
        return this.put(key, new IntTag(value));
    }

    public CompoundTagBuilder putLong(String key, long value)
    {
        return this.put(key, new LongTag(value));
    }

    public CompoundTagBuilder putShort(String key, short value)
    {
        return this.put(key, new ShortTag(value));
    }

    public CompoundTagBuilder putString(String key, String value)
    {
        return this.put(key, new StringTag(value));
    }

    public CompoundTagBuilder putAll(Map<String, ? extends Tag> value)
    {
        checkNotNull(value);

        for (Map.Entry<String, ? extends Tag> entry : value.entrySet())
        {
	        this.put(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public CompoundTag build()
    {
        return new CompoundTag(new HashMap<>(this.entries));
    }

}
