public static void writeEmptyCompoundTag(ByteBuf buf) {
        buf.writeByte(0x0A); // тип CompoundTag
        buf.writeByte(0x00); // конец тега
}

public static void writeTextComponent(ByteBuf buf, String message) {
        buf.writeByte(8); // Тип тега (String) 
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        buf.writeShort(messageBytes.length); // Длина строки
        buf.writeBytes(messageBytes); // Крч это само сообщение
}

public static void writeVarInt(ByteBuf buf, int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                buf.writeByte(value);
                return;
            }
            buf.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
}

public static int readVarInt(ByteBuf buf) {
        int value = 0;
        int shift = 0;
        byte b;
        do {
            b = buf.readByte();
            value |= (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return value;
}

public static void writeString(ByteBuf buf, String str) {
        byte[] bytes = str.getBytes(CharsetUtil.UTF_8);
        writeVarInt(buf, bytes.length);
        buf.writeBytes(bytes);
}

public static String readString(ByteBuf buf) {
        int length = readVarInt(buf);
        if (length > 32767) throw new IllegalArgumentException("String too long");
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes, CharsetUtil.UTF_8);
}
