public void sendChunk(ChannelHandlerContext ctx, int chunkX, int chunkZ) {
        ByteBuf packetBuffer = ctx.alloc().buffer();

        try {
            ByteBuf outBuf = ctx.alloc().buffer();

            // ID пакета
            writeVarInt(outBuf, 0x27);

            // Координаты чанка
            outBuf.writeInt(chunkX);
            outBuf.writeInt(chunkZ);

            // Пустой heightmaps NBT
            writeEmptyCompoundTag(outBuf);

            // Данные чанка
            ByteBuf chunkDataBuf = ctx.alloc().buffer();
            try {
                // 32 секции
                for (int i = 0; i < 32; i++) {
                    chunkDataBuf.writeShort(0);      // block count = 0
                    chunkDataBuf.writeByte(0);       // blocks singular flag
                    chunkDataBuf.writeByte(0);       // block id = 0 (minecraft:air)
                    chunkDataBuf.writeByte(0);       // biome singular flag
                    writeVarInt(chunkDataBuf, 0);    // biome id = 0
                }

                // Дополнительные флаги
                chunkDataBuf.writeByte(0);
                chunkDataBuf.writeByte(0);
                chunkDataBuf.writeByte(0);
                writeVarInt(chunkDataBuf, 0);

                int chunkDataLength = chunkDataBuf.readableBytes();
                writeVarInt(outBuf, chunkDataLength);
                outBuf.writeBytes(chunkDataBuf);
            } finally {
                chunkDataBuf.release();
            }

            // Block entities count = 0
            writeVarInt(outBuf, 0);

            // Light data - все маски пустые
            // Sky Light Mask (0 элементов)
            writeVarInt(outBuf, 0);

            // Block Light Mask (0 элементов)
            writeVarInt(outBuf, 0);

            // Empty Sky Light Mask (0 элементов)
            writeVarInt(outBuf, 0);

            // Empty Block Light Mask (0 элементов)
            writeVarInt(outBuf, 0);

            // Sky Light array count = 0
            writeVarInt(outBuf, 0);

            // Block Light array count = 0
            writeVarInt(outBuf, 0);

            // Создаем финальный пакет с длиной
            writeVarInt(packetBuffer, outBuf.readableBytes());
            packetBuffer.writeBytes(outBuf);

            ctx.writeAndFlush(packetBuffer);
            outBuf.release();

        } catch (Exception e) {
            packetBuffer.release();
            e.printStackTrace();
        }
    }
