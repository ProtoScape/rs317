
import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;

public final class Client extends Applet {

    private static final long serialVersionUID = -7716033016412450361L;

    private static String getFormattedValueName(int value) { // intToKOrMilLongName
        String dest = String.valueOf(value);
        for (int i = dest.length() - 3; i > 0; i -= 3) {
            dest = dest.substring(0, i) + "," + dest.substring(i);
        }
        if (dest.length() > 8) {
            dest = "@gre@" + dest.substring(0, dest.length() - 8) + " million @whi@(" + dest + ")";
        } else if (dest.length() > 4) {
            dest = "@cya@" + dest.substring(0, dest.length() - 4) + "K @whi@(" + dest + ")";
        }
        return " " + dest;
    }

    private void stopMidi() {
        Signlink.midifade = 0;
        Signlink.midi = "stop";
    }

    @SuppressWarnings("unused")
    private void downloadCrcs() {
        int j = 5;
        expectedCRCs[8] = 0;
        int k = 0;
        while (expectedCRCs[8] == 0) {
            String s = "Unknown problem";
            drawLoadingText(20, "Connecting to web server");
            try {
                DataInputStream datainputstream = requestFile("crc" + (int) (Math.random() * 99999999D) + "-" + 317);
                Stream buffer = new Stream(new byte[40]);
                datainputstream.readFully(buffer.payload, 0, 40);
                datainputstream.close();
                for (int i1 = 0; i1 < 9; i1++) {
                    expectedCRCs[i1] = buffer.getInt();
                }
                int j1 = buffer.getInt();
                int k1 = 1234;
                for (int l1 = 0; l1 < 9; l1++) {
                    k1 = (k1 << 1) + expectedCRCs[l1];
                }
                if (j1 != k1) {
                    s = "checksum problem";
                    expectedCRCs[8] = 0;
                }
            } catch (EOFException eofexception) {
                s = "EOF problem";
                expectedCRCs[8] = 0;
            } catch (IOException ioexception) {
                s = "connection problem";
                expectedCRCs[8] = 0;
            } catch (Exception logicexception) {
                s = "logic problem";
                expectedCRCs[8] = 0;
                if (!Signlink.reporterror) {
                    return;
                }
            }
            if (expectedCRCs[8] == 0) {
                k++;
                for (int l = j; l > 0; l--) {
                    if (k >= 10) {
                        drawLoadingText(10, "Game updated - please reload page");
                        l = 10;
                    } else {
                        drawLoadingText(10, s + " - Will retry in " + l + " secs.");
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception exception) {
                    }
                }
                j *= 2;
                if (j > 60) {
                    j = 60;
                }
                aBoolean872 = !aBoolean872;
            }
        }
    }

    private boolean menuHasAddFriend(int arg0) {
        if (arg0 < 0) {
            return false;
        }
        int k = menuActionOpcode[arg0];
        if (k >= 2000) {
            k -= 2000;
        }
        return k == 337;
    }

    private void drawChatArea() {
        aGraphicsBuffer_1166.initDrawingArea();
        Rasterizer.lineOffsets = anIntArray1180;
        chatBack.drawIndexedSprite(0, 0);
        if (messagePromptRaised) {
            boldFont.drawANCText(0, aString1121, 40, 239);
            boldFont.drawANCText(128, promptInput + "*", 60, 239);
        } else if (inputDialogState == 1) {
            boldFont.drawANCText(0, "Enter amount:", 40, 239);
            boldFont.drawANCText(128, amountOrNameInput + "*", 60, 239);
        } else if (inputDialogState == 2) {
            boldFont.drawANCText(0, "Enter name:", 40, 239);
            boldFont.drawANCText(128, amountOrNameInput + "*", 60, 239);
        } else if (aString844 != null) {
            boldFont.drawANCText(0, aString844, 40, 239);
            boldFont.drawANCText(128, "Click to continue", 60, 239);
        } else if (backDialogID != -1) {
            drawInterface(0, 0, Interface.cachedInterfaces[backDialogID], 0);
        } else if (dialogID != -1) {
            drawInterface(0, 0, Interface.cachedInterfaces[dialogID], 0);
        } else {
            Font font = aFont_1271;
            int j = 0;
            Graphics2D.setBounds(77, 0, 463, 0);
            for (int k = 0; k < 100; k++) {
                if (chatMessages[k] != null) {
                    int l = chatTypes[k];
                    int i1 = 70 - j * 14 + chatboxScrollerPos;
                    String s1 = chatNames[k];
                    byte byte0 = 0;
                    if (s1 != null && s1.startsWith("@cr1@")) {
                        s1 = s1.substring(5);
                        byte0 = 1;
                    }
                    if (s1 != null && s1.startsWith("@cr2@")) {
                        s1 = s1.substring(5);
                        byte0 = 2;
                    }
                    if (l == 0) {
                        if (i1 > 0 && i1 < 110) {
                            font.drawText(0, chatMessages[k], i1, 4);
                        }
                        j++;
                    }
                    if ((l == 1 || l == 2) && (l == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s1))) {
                        if (i1 > 0 && i1 < 110) {
                            int j1 = 4;
                            if (byte0 == 1) {
                                modIcons[0].drawIndexedSprite(j1, i1 - 12);
                                j1 += 14;
                            }
                            if (byte0 == 2) {
                                modIcons[1].drawIndexedSprite(j1, i1 - 12);
                                j1 += 14;
                            }
                            font.drawText(0, s1 + ":", i1, j1);
                            j1 += font.getWidth(s1) + 8;
                            font.drawText(255, chatMessages[k], i1, j1);
                        }
                        j++;
                    }
                    if ((l == 3 || l == 7) && splitPrivateChat == 0 && (l == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s1))) {
                        if (i1 > 0 && i1 < 110) {
                            int k1 = 4;
                            font.drawText(0, "From", i1, k1);
                            k1 += font.getWidth("From ");
                            if (byte0 == 1) {
                                modIcons[0].drawIndexedSprite(k1, i1 - 12);
                                k1 += 14;
                            }
                            if (byte0 == 2) {
                                modIcons[1].drawIndexedSprite(k1, i1 - 12);
                                k1 += 14;
                            }
                            font.drawText(0, s1 + ":", i1, k1);
                            k1 += font.getWidth(s1) + 8;
                            font.drawText(0x800000, chatMessages[k], i1, k1);
                        }
                        j++;
                    }
                    if (l == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
                        if (i1 > 0 && i1 < 110) {
                            font.drawText(0x800080, s1 + " " + chatMessages[k], i1, 4);
                        }
                        j++;
                    }
                    if (l == 5 && splitPrivateChat == 0 && privateChatMode < 2) {
                        if (i1 > 0 && i1 < 110) {
                            font.drawText(0x800000, chatMessages[k], i1, 4);
                        }
                        j++;
                    }
                    if (l == 6 && splitPrivateChat == 0 && privateChatMode < 2) {
                        if (i1 > 0 && i1 < 110) {
                            font.drawText(0, "To " + s1 + ":", i1, 4);
                            font.drawText(0x800000, chatMessages[k], i1, 12 + font.getWidth("To " + s1));
                        }
                        j++;
                    }
                    if (l == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
                        if (i1 > 0 && i1 < 110) {
                            font.drawText(0x7e3200, s1 + " " + chatMessages[k], i1, 4);
                        }
                        j++;
                    }
                }
            }
            Graphics2D.resetBounds();
            anInt1211 = j * 14 + 7;
            if (anInt1211 < 78) {
                anInt1211 = 78;
            }
            drawScroller(77, anInt1211 - chatboxScrollerPos - 77, 0, 463, anInt1211);
            String s;
            if (Client.myPlayer != null && Client.myPlayer.name != null) {
                s = Client.myPlayer.name;
            } else {
                s = TextUtil.formatName(myUsername);
            }
            font.drawText(0, s + ":", 90, 4);
            font.drawText(255, inputString + "*", 90, 6 + font.getWidth(s + ": "));
            Graphics2D.drawHorizontalLine(77, 0, 479, 0);
        }
        if (menuOpen && menuScreenArea == 2) {
            drawMenu();
        }
        aGraphicsBuffer_1166.drawGraphics(357, super.graphics, 17);
        gameScreenCanvas.initDrawingArea();
        Rasterizer.lineOffsets = anIntArray1182;
    }

    public void init() {
        Client.nodeID = Integer.parseInt(getParameter("nodeid"));
        Client.portOff = Integer.parseInt(getParameter("portoff"));
        String s = getParameter("lowmem");
        if (s != null && s.equals("1")) {
            Client.setLowMem();
        } else {
            Client.setHighMem();
        }
        String s1 = getParameter("free");
        Client.isMembers = !(s1 != null && s1.equals("1"));
        initGameApplet(503, 765);
    }

    public void startRunnable(Runnable runnable, int priority) {
        if (priority > 10) {
            priority = 10;
        }
        if (Signlink.mainapp != null) {
            Signlink.startthread(runnable, priority);
        } else {
            super.startRunnable(runnable, priority);
        }
    }

    public java.net.Socket openSocket(int socketId) throws IOException {
        if (Signlink.mainapp != null) {
            return Signlink.opensocket(socketId);
        } else {
            return new java.net.Socket(InetAddress.getByName(getCodeBase().getHost()), socketId);
        }
    }

    private void processMenuClick() {
        if (activeInterfaceType != 0) {
            return;
        }
        int j = super.clickMode3;
        if (spellSelected == 1 && super.saveClickX >= 516 && super.saveClickY >= 160 && super.saveClickX <= 765 && super.saveClickY <= 205) {
            j = 0;
        }
        if (menuOpen) {
            if (j != 1) {
                int k = super.mouseX;
                int j1 = super.mouseY;
                if (menuScreenArea == 0) {
                    k -= 4;
                    j1 -= 4;
                }
                if (menuScreenArea == 1) {
                    k -= 553;
                    j1 -= 205;
                }
                if (menuScreenArea == 2) {
                    k -= 17;
                    j1 -= 357;
                }
                if (k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10 || j1 < menuOffsetY - 10 || j1 > menuOffsetY + anInt952 + 10) {
                    menuOpen = false;
                    if (menuScreenArea == 1) {
                        needDrawTabArea = true;
                    }
                    if (menuScreenArea == 2) {
                        inputTaken = true;
                    }
                }
            }
            if (j == 1) {
                int l = menuOffsetX;
                int k1 = menuOffsetY;
                int i2 = menuWidth;
                int k2 = super.saveClickX;
                int l2 = super.saveClickY;
                if (menuScreenArea == 0) {
                    k2 -= 4;
                    l2 -= 4;
                }
                if (menuScreenArea == 1) {
                    k2 -= 553;
                    l2 -= 205;
                }
                if (menuScreenArea == 2) {
                    k2 -= 17;
                    l2 -= 357;
                }
                int i3 = -1;
                for (int j3 = 0; j3 < menuActionCount; j3++) {
                    int k3 = k1 + 31 + (menuActionCount - 1 - j3) * 15;
                    if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3) {
                        i3 = j3;
                    }
                }
                if (i3 != -1) {
                    doAction(i3);
                }
                menuOpen = false;
                if (menuScreenArea == 1) {
                    needDrawTabArea = true;
                }
                if (menuScreenArea == 2) {
                    inputTaken = true;
                }
            }
        } else {
            if (j == 1 && menuActionCount > 0) {
                int i1 = menuActionOpcode[menuActionCount - 1];
                if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447 || i1 == 1125) {
                    int l1 = menuActionCmd2[menuActionCount - 1];
                    int j2 = menuActionCmd3[menuActionCount - 1];
                    Interface component = Interface.cachedInterfaces[j2];
                    if (component.aBoolean259 || component.aBoolean235) {
                        aBoolean1242 = false;
                        anInt989 = 0;
                        anInt1084 = j2;
                        anInt1085 = l1;
                        activeInterfaceType = 2;
                        anInt1087 = super.saveClickX;
                        anInt1088 = super.saveClickY;
                        if (Interface.cachedInterfaces[j2].parentId == openInterfaceId) {
                            activeInterfaceType = 1;
                        }
                        if (Interface.cachedInterfaces[j2].parentId == backDialogID) {
                            activeInterfaceType = 3;
                        }
                        return;
                    }
                }
            }
            if (j == 1 && (anInt1253 == 1 || menuHasAddFriend(menuActionCount - 1)) && menuActionCount > 2) {
                j = 2;
            }
            if (j == 1 && menuActionCount > 0) {
                doAction(menuActionCount - 1);
            }
            if (j == 2 && menuActionCount > 0) {
                determineMenuSize();
            }
        }
    }

    private void saveMidi(boolean flag, byte buf[]) {
        Signlink.midifade = flag ? 1 : 0;
        Signlink.midisave(buf, buf.length);
    }

    private void method22() {
        try {
            anInt985 = -1;
            stillGraphicList.clear();
            projectileList.clear();
            Rasterizer.clearTextures();
            clearCache();
            sceneGraph.reset();
            System.gc();
            for (int i = 0; i < 4; i++) {
                collisionMaps[i].reset();
            }
            for (int l = 0; l < 4; l++) {
                for (int k1 = 0; k1 < 104; k1++) {
                    for (int j2 = 0; j2 < 104; j2++) {
                        byteGroundArray[l][k1][j2] = 0;
                    }
                }
            }
            Region region = new Region(byteGroundArray, intGroundArray);
            int k2 = aByteArrayArray1183.length;
            outputStream.writeOpcode(0);
            if (!aBoolean1159) {
                for (int i3 = 0; i3 < k2; i3++) {
                    int i4 = (anIntArray1234[i3] >> 8) * 64 - baseX;
                    int k5 = (anIntArray1234[i3] & 0xff) * 64 - baseY;
                    byte abyte0[] = aByteArrayArray1183[i3];
                    if (abyte0 != null) {
                        region.method180(abyte0, k5, i4, (anInt1069 - 6) * 8, (anInt1070 - 6) * 8, collisionMaps);
                    }
                }
                for (int j4 = 0; j4 < k2; j4++) {
                    int l5 = (anIntArray1234[j4] >> 8) * 64 - baseX;
                    int k7 = (anIntArray1234[j4] & 0xff) * 64 - baseY;
                    byte abyte2[] = aByteArrayArray1183[j4];
                    if (abyte2 == null && anInt1070 < 800) {
                        region.method174(k7, 64, 64, l5);
                    }
                }
                Client.anInt1097++;
                if (Client.anInt1097 > 160) {
                    Client.anInt1097 = 0;
                    outputStream.writeOpcode(238);
                    outputStream.writeByte(96);
                }
                outputStream.writeOpcode(0);
                for (int i6 = 0; i6 < k2; i6++) {
                    byte abyte1[] = aByteArrayArray1247[i6];
                    if (abyte1 != null) {
                        int l8 = (anIntArray1234[i6] >> 8) * 64 - baseX;
                        int k9 = (anIntArray1234[i6] & 0xff) * 64 - baseY;
                        region.method190(l8, collisionMaps, k9, sceneGraph, abyte1);
                    }
                }
            }
            if (aBoolean1159) {
                for (int j3 = 0; j3 < 4; j3++) {
                    for (int k4 = 0; k4 < 13; k4++) {
                        for (int j6 = 0; j6 < 13; j6++) {
                            int l7 = anIntArrayArrayArray1129[j3][k4][j6];
                            if (l7 != -1) {
                                int i9 = l7 >> 24 & 3;
                                int l9 = l7 >> 1 & 3;
                                int j10 = l7 >> 14 & 0x3ff;
                                int l10 = l7 >> 3 & 0x7ff;
                                int j11 = (j10 / 8 << 8) + l10 / 8;
                                for (int l11 = 0; l11 < anIntArray1234.length; l11++) {
                                    if (anIntArray1234[l11] != j11 || aByteArrayArray1183[l11] == null) {
                                        continue;
                                    }
                                    region.method179(i9, l9, collisionMaps, k4 * 8, (j10 & 7) * 8, aByteArrayArray1183[l11], (l10 & 7) * 8, j3, j6 * 8);
                                    break;
                                }
                            }
                        }
                    }
                }
                for (int l4 = 0; l4 < 13; l4++) {
                    for (int k6 = 0; k6 < 13; k6++) {
                        int i8 = anIntArrayArrayArray1129[0][l4][k6];
                        if (i8 == -1) {
                            region.method174(k6 * 8, 8, 8, l4 * 8);
                        }
                    }
                }
                outputStream.writeOpcode(0);
                for (int l6 = 0; l6 < 4; l6++) {
                    for (int j8 = 0; j8 < 13; j8++) {
                        for (int j9 = 0; j9 < 13; j9++) {
                            int i10 = anIntArrayArrayArray1129[l6][j8][j9];
                            if (i10 != -1) {
                                int k10 = i10 >> 24 & 3;
                                int i11 = i10 >> 1 & 3;
                                int k11 = i10 >> 14 & 0x3ff;
                                int i12 = i10 >> 3 & 0x7ff;
                                int j12 = (k11 / 8 << 8) + i12 / 8;
                                for (int k12 = 0; k12 < anIntArray1234.length; k12++) {
                                    if (anIntArray1234[k12] != j12 || aByteArrayArray1247[k12] == null) {
                                        continue;
                                    }
                                    region.method183(collisionMaps, sceneGraph, k10, j8 * 8, (i12 & 7) * 8, l6, aByteArrayArray1247[k12], (k11 & 7) * 8, i11, j9 * 8);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            outputStream.writeOpcode(0);
            region.method171(collisionMaps, sceneGraph);
            gameScreenCanvas.initDrawingArea();
            outputStream.writeOpcode(0);
            int k3 = Region.anInt145;
            if (k3 > plane) {
                k3 = plane;
            }
            if (k3 < plane - 1) {
                k3 = plane - 1;
            }
            if (Client.lowMem) {
                sceneGraph.method275(Region.anInt145);
            } else {
                sceneGraph.method275(0);
            }
            for (int i5 = 0; i5 < 104; i5++) {
                for (int i7 = 0; i7 < 104; i7++) {
                    spawnGroundItem(i5, i7);
                }
            }
            Client.anInt1051++;
            if (Client.anInt1051 > 98) {
                Client.anInt1051 = 0;
                outputStream.writeOpcode(150);
            }
            method63();
        } catch (Exception exception) {
        }
        ObjectDefinition.cache1.clear();
        if (super.frame != null) {
            outputStream.writeOpcode(210);
            outputStream.writeInt(0x3f008edd);
        }
        if (Client.lowMem && Signlink.cache_dat != null) {
            int j = onDemandFetcher.getFileCount(0);
            for (int i1 = 0; i1 < j; i1++) {
                int l1 = onDemandFetcher.getModelIndex(i1);
                if ((l1 & 0x79) == 0) {
                    Model.resetModel(i1);
                }
            }
        }
        System.gc();
        Rasterizer.resetTextures();
        onDemandFetcher.method566();
        int k = (anInt1069 - 6) / 8 - 1;
        int j1 = (anInt1069 + 6) / 8 + 1;
        int i2 = (anInt1070 - 6) / 8 - 1;
        int l2 = (anInt1070 + 6) / 8 + 1;
        if (aBoolean1141) {
            k = 49;
            j1 = 50;
            i2 = 49;
            l2 = 50;
        }
        for (int l3 = k; l3 <= j1; l3++) {
            for (int j5 = i2; j5 <= l2; j5++) {
                if (l3 == k || l3 == j1 || j5 == i2 || j5 == l2) {
                    int j7 = onDemandFetcher.getMapCount(0, j5, l3);
                    if (j7 != -1) {
                        onDemandFetcher.method560(j7, 3);
                    }
                    int k8 = onDemandFetcher.getMapCount(1, j5, l3);
                    if (k8 != -1) {
                        onDemandFetcher.method560(k8, 3);
                    }
                }
            }
        }
    }

    /**
     * The resetting of data in the memory cache upon entering a new region.
     */
    private void clearCache() {// unlinkMRUNodes()
        ObjectDefinition.cache1.clear();
        ObjectDefinition.cache2.clear();
        NpcDefinition.mruNodes.clear();
        ItemDefinition.modelCache.clear();
        ItemDefinition.spriteCache.clear();
        Player.modelCache.clear();
        SpotAnimation.cache.clear();
    }

    private void method24(int arg0) {
        int ai[] = minimap.pixels;
        int j = ai.length;
        for (int k = 0; k < j; k++) {
            ai[k] = 0;
        }
        for (int l = 1; l < 103; l++) {
            int i1 = 24628 + (103 - l) * 512 * 4;
            for (int k1 = 1; k1 < 103; k1++) {
                if ((byteGroundArray[arg0][k1][l] & 0x18) == 0) {
                    sceneGraph.method309(ai, i1, arg0, k1, l);
                }
                if (arg0 < 3 && (byteGroundArray[arg0 + 1][k1][l] & 8) != 0) {
                    sceneGraph.method309(ai, i1, arg0 + 1, k1, l);
                }
                i1 += 4;
            }
        }
        int j1 = (238 + (int) (Math.random() * 20D) - 10 << 16) + (238 + (int) (Math.random() * 20D) - 10 << 8) + 238 + (int) (Math.random() * 20D) - 10;
        int l1 = 238 + (int) (Math.random() * 20D) - 10 << 16;
        minimap.init();
        for (int i2 = 1; i2 < 103; i2++) {
            for (int j2 = 1; j2 < 103; j2++) {
                if ((byteGroundArray[arg0][j2][i2] & 0x18) == 0) {
                    method50(i2, j1, j2, l1, arg0);
                }
                if (arg0 < 3 && (byteGroundArray[arg0 + 1][j2][i2] & 8) != 0) {
                    method50(i2, j1, j2, l1, arg0 + 1);
                }
            }
        }
        gameScreenCanvas.initDrawingArea();
        anInt1071 = 0;
        for (int k2 = 0; k2 < 104; k2++) {
            for (int l2 = 0; l2 < 104; l2++) {
                int i3 = sceneGraph.getGroundDecorationUid(plane, k2, l2);
                if (i3 != 0) {
                    i3 = i3 >> 14 & 0x7fff;
                    int j3 = ObjectDefinition.forId(i3).mapFunctionId;
                    if (j3 >= 0) {
                        int k3 = k2;
                        int l3 = l2;
                        if (j3 != 22 && j3 != 29 && j3 != 34 && j3 != 36 && j3 != 46 && j3 != 47 && j3 != 48) {
                            byte byte0 = 104;
                            byte byte1 = 104;
                            int ai1[][] = collisionMaps[plane].collisionFlags;
                            for (int i4 = 0; i4 < 10; i4++) {
                                int j4 = (int) (Math.random() * 4D);
                                if (j4 == 0 && k3 > 0 && k3 > k2 - 3 && (ai1[k3 - 1][l3] & 0x1280108) == 0) {
                                    k3--;
                                }
                                if (j4 == 1 && k3 < byte0 - 1 && k3 < k2 + 3 && (ai1[k3 + 1][l3] & 0x1280180) == 0) {
                                    k3++;
                                }
                                if (j4 == 2 && l3 > 0 && l3 > l2 - 3 && (ai1[k3][l3 - 1] & 0x1280102) == 0) {
                                    l3--;
                                }
                                if (j4 == 3 && l3 < byte1 - 1 && l3 < l2 + 3 && (ai1[k3][l3 + 1] & 0x1280120) == 0) {
                                    l3++;
                                }
                            }
                        }
                        aClass30_Sub2_Sub1_Sub1Array1140[anInt1071] = mapFunctions[j3];
                        anIntArray1072[anInt1071] = k3;
                        anIntArray1073[anInt1071] = l3;
                        anInt1071++;
                    }
                }
            }
        }
    }

    private void spawnGroundItem(int arg1, int arg2) {
        Deque class19 = groundItems[plane][arg1][arg2];
        if (class19 == null) {
            sceneGraph.resetItemPile(plane, arg1, arg2);
            return;
        }
        int k = 0xfa0a1f01;
        Object obj = null;
        for (GroundItem groundItem = (GroundItem) class19.getFront(); groundItem != null; groundItem = (GroundItem) class19.getNext()) {
            ItemDefinition itemDefinition = ItemDefinition.forId(groundItem.id);
            int l = itemDefinition.value;
            if (itemDefinition.stackable) {
                l *= groundItem.amount + 1;
            }
            // notifyItemSpawn(item, i + baseX, j + baseY);
            if (l > k) {
                k = l;
                obj = groundItem;
            }
        }
        class19.insertFront(((Node) obj));
        Object obj1 = null;
        Object obj2 = null;
        for (GroundItem class30_sub2_sub4_sub2_1 = (GroundItem) class19.getFront(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (GroundItem) class19.getNext()) {
            if (class30_sub2_sub4_sub2_1.id != ((GroundItem) obj).id && obj1 == null) {
                obj1 = class30_sub2_sub4_sub2_1;
            }
            if (class30_sub2_sub4_sub2_1.id != ((GroundItem) obj).id && class30_sub2_sub4_sub2_1.id != ((GroundItem) obj1).id && obj2 == null) {
                obj2 = class30_sub2_sub4_sub2_1;
            }
        }
        int i1 = arg1 + (arg2 << 7) + 0x60000000;
        sceneGraph.method281(arg1, i1, ((Entity) obj1), method42(plane, arg2 * 128 + 64, arg1 * 128 + 64), ((Entity) obj2), ((Entity) obj), plane, arg2);
    }

    private void renderNpcs(boolean flag) {
        for (int j = 0; j < localNpcCount; j++) {
            Npc npc = localNpcs[localNpcIndices[j]];
            int k = 0x20000000 + (localNpcIndices[j] << 14);
            if (npc == null || !npc.isVisible() || npc.desc.members != flag) {
                continue;
            }
            int l = npc.x >> 7;
            int i1 = npc.y >> 7;
            if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104) {
                continue;
            }
            if (npc.anInt1540 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
                if (anIntArrayArray929[l][i1] == flashEffectCycle) {
                    continue;
                }
                anIntArrayArray929[l][i1] = flashEffectCycle;
            }
            if (!npc.desc.clickable) {
                k += 0x80000000;
            }
            sceneGraph.method285(plane, npc.anInt1552, method42(plane, npc.y, npc.x), k, npc.y, (npc.anInt1540 - 1) * 64 + 60, npc.x, npc, npc.aBoolean1541);
        }
    }

    private boolean replayWave() {
        return Signlink.wavereplay();
    }

    private void throwLoadError() {
        String s = "ondemand";// was a constant parameter
        System.out.println(s);
        try {
            getAppletContext().showDocument(new URL(getCodeBase(), "loaderror_" + s + ".html"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        do {
            try {
                Thread.sleep(1000L);
            } catch (Exception exception) {
            }
        } while (true);
    }

    private void buildInterfaceMenu(int arg0, Interface interfaceInstance, int arg2, int arg3, int arg4, int arg5) {
        if (interfaceInstance.interfaceType != 0 || interfaceInstance.children == null || interfaceInstance.aBoolean266) {
            return;
        }
        if (arg2 < arg0 || arg4 < arg3 || arg2 > arg0 + interfaceInstance.width || arg4 > arg3 + interfaceInstance.height) {
            return;
        }
        int k1 = interfaceInstance.children.length;
        for (int l1 = 0; l1 < k1; l1++) {
            int i2 = interfaceInstance.childX[l1] + arg0;
            int j2 = interfaceInstance.childY[l1] + arg3 - arg5;
            Interface component = Interface.cachedInterfaces[interfaceInstance.children[l1]];
            i2 += component.anInt263;
            j2 += component.anInt265;
            if ((component.hoverType >= 0 || component.anInt216 != 0) && arg2 >= i2 && arg4 >= j2 && arg2 < i2 + component.width && arg4 < j2 + component.height) {
                if (component.hoverType >= 0) {
                    anInt886 = component.hoverType;
                } else {
                    anInt886 = component.id;
                }
            }
            if (component.interfaceType == 0) {
                buildInterfaceMenu(i2, component, arg2, j2, arg4, component.scrollPosition);
                if (component.scrollMax > component.height) {
                    method65(i2 + component.width, component.height, arg2, arg4, component, j2, true, component.scrollMax);
                }
            } else {
                if (component.actionType == 1 && arg2 >= i2 && arg4 >= j2 && arg2 < i2 + component.width && arg4 < j2 + component.height) {
                    boolean flag = false;
                    if (component.contentType != 0) {
                        flag = buildFriendsListMenu(component);
                    }
                    if (!flag) {
                        menuActionName[menuActionCount] = component.tooltip;
                        menuActionOpcode[menuActionCount] = 315;
                        menuActionCmd3[menuActionCount] = component.id;
                        menuActionCount++;
                    }
                }
                if (component.actionType == 2 && spellSelected == 0 && arg2 >= i2 && arg4 >= j2 && arg2 < i2 + component.width && arg4 < j2 + component.height) {
                    String s = component.selectedActionName;
                    if (s.indexOf(" ") != -1) {
                        s = s.substring(0, s.indexOf(" "));
                    }
                    menuActionName[menuActionCount] = s + " @gre@" + component.spellName;
                    menuActionOpcode[menuActionCount] = 626;
                    menuActionCmd3[menuActionCount] = component.id;
                    menuActionCount++;
                }
                if (component.actionType == 3 && arg2 >= i2 && arg4 >= j2 && arg2 < i2 + component.width && arg4 < j2 + component.height) {
                    menuActionName[menuActionCount] = "Close";
                    menuActionOpcode[menuActionCount] = 200;
                    menuActionCmd3[menuActionCount] = component.id;
                    menuActionCount++;
                }
                if (component.actionType == 4 && arg2 >= i2 && arg4 >= j2 && arg2 < i2 + component.width && arg4 < j2 + component.height) {
                    menuActionName[menuActionCount] = component.tooltip;
                    menuActionOpcode[menuActionCount] = 169;
                    menuActionCmd3[menuActionCount] = component.id;
                    menuActionCount++;
                }
                if (component.actionType == 5 && arg2 >= i2 && arg4 >= j2 && arg2 < i2 + component.width && arg4 < j2 + component.height) {
                    menuActionName[menuActionCount] = component.tooltip;
                    menuActionOpcode[menuActionCount] = 646;
                    menuActionCmd3[menuActionCount] = component.id;
                    menuActionCount++;
                }
                if (component.actionType == 6 && !aBoolean1149 && arg2 >= i2 && arg4 >= j2 && arg2 < i2 + component.width && arg4 < j2 + component.height) {
                    menuActionName[menuActionCount] = component.tooltip;
                    menuActionOpcode[menuActionCount] = 679;
                    menuActionCmd3[menuActionCount] = component.id;
                    menuActionCount++;
                }
                if (component.interfaceType == 2) {
                    int k2 = 0;
                    for (int l2 = 0; l2 < component.height; l2++) {
                        for (int i3 = 0; i3 < component.width; i3++) {
                            int j3 = i2 + i3 * (32 + component.invSpritePadX);
                            int k3 = j2 + l2 * (32 + component.invSpritePadY);
                            if (k2 < 20) {
                                j3 += component.spritesX[k2];
                                k3 += component.spritesY[k2];
                            }
                            if (arg2 >= j3 && arg4 >= k3 && arg2 < j3 + 32 && arg4 < k3 + 32) {
                                mouseInvInterfaceIndex = k2;
                                lastActiveInvInterface = component.id;
                                if (component.inv[k2] > 0) {
                                    ItemDefinition itemDefinition = ItemDefinition.forId(component.inv[k2] - 1);
                                    if (itemSelected == 1 && component.isInventoryInterface) {
                                        if (component.id != anInt1284 || k2 != anInt1283) {
                                            menuActionName[menuActionCount] = "Use " + selectedItemName + " with @lre@" + itemDefinition.name;
                                            menuActionOpcode[menuActionCount] = 870;
                                            menuActionCmd1[menuActionCount] = itemDefinition.id;
                                            menuActionCmd2[menuActionCount] = k2;
                                            menuActionCmd3[menuActionCount] = component.id;
                                            menuActionCount++;
                                        }
                                    } else if (spellSelected == 1 && component.isInventoryInterface) {
                                        if ((spellUsableOn & 0x10) == 16) {
                                            menuActionName[menuActionCount] = spellTooltip + " @lre@" + itemDefinition.name;
                                            menuActionOpcode[menuActionCount] = 543;
                                            menuActionCmd1[menuActionCount] = itemDefinition.id;
                                            menuActionCmd2[menuActionCount] = k2;
                                            menuActionCmd3[menuActionCount] = component.id;
                                            menuActionCount++;
                                        }
                                    } else {
                                        if (component.isInventoryInterface) {
                                            for (int l3 = 4; l3 >= 3; l3--) {
                                                if (itemDefinition.inventoryActions != null && itemDefinition.inventoryActions[l3] != null) {
                                                    menuActionName[menuActionCount] = itemDefinition.inventoryActions[l3] + " @lre@" + itemDefinition.name;
                                                    if (l3 == 3) {
                                                        menuActionOpcode[menuActionCount] = 493;
                                                    }
                                                    if (l3 == 4) {
                                                        menuActionOpcode[menuActionCount] = 847;
                                                    }
                                                    menuActionCmd1[menuActionCount] = itemDefinition.id;
                                                    menuActionCmd2[menuActionCount] = k2;
                                                    menuActionCmd3[menuActionCount] = component.id;
                                                    menuActionCount++;
                                                } else if (l3 == 4) {
                                                    menuActionName[menuActionCount] = "Drop @lre@" + itemDefinition.name;
                                                    menuActionOpcode[menuActionCount] = 847;
                                                    menuActionCmd1[menuActionCount] = itemDefinition.id;
                                                    menuActionCmd2[menuActionCount] = k2;
                                                    menuActionCmd3[menuActionCount] = component.id;
                                                    menuActionCount++;
                                                }
                                            }
                                        }
                                        if (component.usableItemInterface) {
                                            menuActionName[menuActionCount] = "Use @lre@" + itemDefinition.name;
                                            menuActionOpcode[menuActionCount] = 447;
                                            menuActionCmd1[menuActionCount] = itemDefinition.id;
                                            menuActionCmd2[menuActionCount] = k2;
                                            menuActionCmd3[menuActionCount] = component.id;
                                            menuActionCount++;
                                        }
                                        if (component.isInventoryInterface && itemDefinition.inventoryActions != null) {
                                            for (int i4 = 2; i4 >= 0; i4--) {
                                                if (itemDefinition.inventoryActions[i4] != null) {
                                                    menuActionName[menuActionCount] = itemDefinition.inventoryActions[i4] + " @lre@" + itemDefinition.name;
                                                    if (i4 == 0) {
                                                        menuActionOpcode[menuActionCount] = 74;
                                                    }
                                                    if (i4 == 1) {
                                                        menuActionOpcode[menuActionCount] = 454;
                                                    }
                                                    if (i4 == 2) {
                                                        menuActionOpcode[menuActionCount] = 539;
                                                    }
                                                    menuActionCmd1[menuActionCount] = itemDefinition.id;
                                                    menuActionCmd2[menuActionCount] = k2;
                                                    menuActionCmd3[menuActionCount] = component.id;
                                                    menuActionCount++;
                                                }
                                            }
                                        }
                                        if (component.actions != null) {
                                            for (int j4 = 4; j4 >= 0; j4--) {
                                                if (component.actions[j4] != null) {
                                                    menuActionName[menuActionCount] = component.actions[j4] + " @lre@" + itemDefinition.name;
                                                    if (j4 == 0) {
                                                        menuActionOpcode[menuActionCount] = 632;
                                                    }
                                                    if (j4 == 1) {
                                                        menuActionOpcode[menuActionCount] = 78;
                                                    }
                                                    if (j4 == 2) {
                                                        menuActionOpcode[menuActionCount] = 867;
                                                    }
                                                    if (j4 == 3) {
                                                        menuActionOpcode[menuActionCount] = 431;
                                                    }
                                                    if (j4 == 4) {
                                                        menuActionOpcode[menuActionCount] = 53;
                                                    }
                                                    menuActionCmd1[menuActionCount] = itemDefinition.id;
                                                    menuActionCmd2[menuActionCount] = k2;
                                                    menuActionCmd3[menuActionCount] = component.id;
                                                    menuActionCount++;
                                                }
                                            }
                                        }
                                        menuActionName[menuActionCount] = "Examine @lre@" + itemDefinition.name;
                                        menuActionOpcode[menuActionCount] = 1125;
                                        menuActionCmd1[menuActionCount] = itemDefinition.id;
                                        menuActionCmd2[menuActionCount] = k2;
                                        menuActionCmd3[menuActionCount] = component.id;
                                        menuActionCount++;
                                    }
                                }
                            }
                            k2++;
                        }
                    }
                }
            }
        }
    }

    private void drawScroller(int arg0, int arg1, int arg2, int arg3, int arg4) {// method30
        scrollBar1.drawIndexedSprite(arg3, arg2);
        scrollBar2.drawIndexedSprite(arg3, arg2 + arg0 - 16);
        Graphics2D.fillRect(arg0 - 32, arg2 + 16, arg3, anInt1002, 16);
        int k1 = (arg0 - 32) * arg0 / arg4;
        if (k1 < 8) {
            k1 = 8;
        }
        int l1 = (arg0 - 32 - k1) * arg1 / (arg4 - arg0);
        Graphics2D.fillRect(k1, arg2 + 16 + l1, arg3, anInt1063, 16);
        Graphics2D.drawVerticalLine(arg2 + 16 + l1, anInt902, k1, arg3);
        Graphics2D.drawVerticalLine(arg2 + 16 + l1, anInt902, k1, arg3 + 1);
        Graphics2D.drawHorizontalLine(arg2 + 16 + l1, anInt902, 16, arg3);
        Graphics2D.drawHorizontalLine(arg2 + 17 + l1, anInt902, 16, arg3);
        Graphics2D.drawVerticalLine(arg2 + 16 + l1, anInt927, k1, arg3 + 15);
        Graphics2D.drawVerticalLine(arg2 + 17 + l1, anInt927, k1 - 1, arg3 + 14);
        Graphics2D.drawHorizontalLine(arg2 + 15 + l1 + k1, anInt927, 16, arg3);
        Graphics2D.drawHorizontalLine(arg2 + 14 + l1 + k1, anInt927, 15, arg3 + 1);
    }

    private void updateNPCs(Stream stream, int arg1) {
        localEntityCount = 0;
        entityUpdateCount = 0;
        updateNpcMovement(stream);
        method46(arg1, stream);
        parseNpcUpdateFlags(stream);
        for (int k = 0; k < localEntityCount; k++) {
            int l = localPlayerIndices[k];
            if (localNpcs[l].lastUpdate != Client.loopCycle) {
                localNpcs[l].desc = null;
                localNpcs[l] = null;
            }
        }
        if (stream.offset != arg1) {
            Signlink.reporterror(myUsername + " size mismatch in getnpcpos - pos:" + stream.offset + " psize:" + arg1);
            throw new RuntimeException("eek");
        }
        for (int i1 = 0; i1 < localNpcCount; i1++) {
            if (localNpcs[localNpcIndices[i1]] == null) {
                Signlink.reporterror(myUsername + " null entry in npc list - pos:" + i1 + " size:" + localNpcCount);
                throw new RuntimeException("eek");
            }
        }
    }

    private void processChatModeClick() {
        if (super.clickMode3 == 1) {
            if (super.saveClickX >= 6 && super.saveClickX <= 106 && super.saveClickY >= 467 && super.saveClickY <= 499) {
                publicChatMode = (publicChatMode + 1) % 4;
                aBoolean1233 = true;
                inputTaken = true;
                outputStream.writeOpcode(95);
                outputStream.writeByte(publicChatMode);
                outputStream.writeByte(privateChatMode);
                outputStream.writeByte(tradeMode);
            }
            if (super.saveClickX >= 135 && super.saveClickX <= 235 && super.saveClickY >= 467 && super.saveClickY <= 499) {
                privateChatMode = (privateChatMode + 1) % 3;
                aBoolean1233 = true;
                inputTaken = true;
                outputStream.writeOpcode(95);
                outputStream.writeByte(publicChatMode);
                outputStream.writeByte(privateChatMode);
                outputStream.writeByte(tradeMode);
            }
            if (super.saveClickX >= 273 && super.saveClickX <= 373 && super.saveClickY >= 467 && super.saveClickY <= 499) {
                tradeMode = (tradeMode + 1) % 3;
                aBoolean1233 = true;
                inputTaken = true;
                outputStream.writeOpcode(95);
                outputStream.writeByte(publicChatMode);
                outputStream.writeByte(privateChatMode);
                outputStream.writeByte(tradeMode);
            }
            if (super.saveClickX >= 412 && super.saveClickX <= 512 && super.saveClickY >= 467 && super.saveClickY <= 499) {
                if (openInterfaceId == -1) {
                    closeOpenInterfaces();
                    reportAbuseInput = "";
                    canMute = false;
                    for (Interface element : Interface.cachedInterfaces) {
                        if (element == null || element.contentType != 600) {
                            continue;
                        }
                        reportAbuseInterfaceID = openInterfaceId = element.parentId;
                        break;
                    }
                } else {
                    pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
                }
            }
            Client.anInt940++;
            if (Client.anInt940 > 1386) {
                Client.anInt940 = 0;
                outputStream.writeOpcode(165);
                outputStream.writeByte(0);
                int j = outputStream.offset;
                outputStream.writeByte(139);
                outputStream.writeByte(150);
                outputStream.writeShort(32131);
                outputStream.writeByte((int) (Math.random() * 256D));
                outputStream.writeShort(3250);
                outputStream.writeByte(177);
                outputStream.writeShort(24859);
                outputStream.writeByte(119);
                if ((int) (Math.random() * 2D) == 0) {
                    outputStream.writeShort(47234);
                }
                if ((int) (Math.random() * 2D) == 0) {
                    outputStream.writeByte(21);
                }
                outputStream.writeSizeByte(outputStream.offset - j);
            }
        }
    }

    private void method33(int i) {
        int j = Varp.cache[i].anInt709;
        if (j == 0) {
            return;
        }
        int k = configStates[i];
        if (j == 1) {
            if (k == 1) {
                Rasterizer.generatePalette(0.90000000000000002D);
            }
            if (k == 2) {
                Rasterizer.generatePalette(0.80000000000000004D);
            }
            if (k == 3) {
                Rasterizer.generatePalette(0.69999999999999996D);
            }
            if (k == 4) {
                Rasterizer.generatePalette(0.59999999999999998D);
            }
            ItemDefinition.spriteCache.clear();
            welcomeScreenRaised = true;
        }
        if (j == 3) {
            boolean flag1 = musicEnabled;
            if (k == 0) {
                adjustVolume(musicEnabled, 0);
                musicEnabled = true;
            }
            if (k == 1) {
                adjustVolume(musicEnabled, -400);
                musicEnabled = true;
            }
            if (k == 2) {
                adjustVolume(musicEnabled, -800);
                musicEnabled = true;
            }
            if (k == 3) {
                adjustVolume(musicEnabled, -1200);
                musicEnabled = true;
            }
            if (k == 4) {
                musicEnabled = false;
            }
            if (musicEnabled != flag1 && !Client.lowMem) {
                if (musicEnabled) {
                    nextSong = currentSong;
                    songChanging = true;
                    onDemandFetcher.method558(2, nextSong);
                } else {
                    stopMidi();
                }
                prevSong = 0;
            }
        }
        if (j == 4) {
            if (k == 0) {
                aBoolean848 = true;
                setWaveVolume(0);
            }
            if (k == 1) {
                aBoolean848 = true;
                setWaveVolume(-400);
            }
            if (k == 2) {
                aBoolean848 = true;
                setWaveVolume(-800);
            }
            if (k == 3) {
                aBoolean848 = true;
                setWaveVolume(-1200);
            }
            if (k == 4) {
                aBoolean848 = false;
            }
        }
        if (j == 5) {
            anInt1253 = k;
        }
        if (j == 6) {
            anInt1249 = k;
        }
        if (j == 8) {
            splitPrivateChat = k;
            inputTaken = true;
        }
        if (j == 9) {
            anInt913 = k;
        }
    }

    private void updateEntities() {
        try {
            int anInt974 = 0;
            for (int id = -1; id < localPlayerCount + localNpcCount; id++) {
                Object obj;
                if (id == -1) {
                    obj = myPlayer;
                } else if (id < localPlayerCount) {
                    obj = localPlayers[playerIndices[id]];
                } else {
                    obj = localNpcs[localNpcIndices[id - localPlayerCount]];
                }
                if (obj == null || !((Mobile) (obj)).isVisible()) {
                    continue;
                }
                if (obj instanceof Npc) {
                    NpcDefinition npcDefinition = ((Npc) obj).desc;
                    if (npcDefinition.childIds != null) {
                        npcDefinition = npcDefinition.method161();
                    }
                    if (npcDefinition == null) {
                        continue;
                    }
                }
                if (id < localPlayerCount) {
                    int l = 30;
                    Player player = (Player) obj;
                    if (player.skullIcon != -1 || player.prayerIcon != -1) {
                        worldToScreen(((Mobile) (obj)), ((Mobile) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            if (player.skullIcon != -1) {
                                skullIcon[player.skullIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
                                l += 25;
                            }
                            if (player.prayerIcon != -1) {
                                prayerIcon[player.prayerIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
                                l += 25;
                            }
                        }
                    }
                    if (id >= 0 && anInt855 == 10 && anInt933 == playerIndices[id]) {
                        worldToScreen(((Mobile) (obj)), ((Mobile) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            hintIcon[1].drawSprite(spriteDrawX - 12, spriteDrawY - l);
                        }
                    }
                } else {
                    NpcDefinition npcDefinition_1 = ((Npc) obj).desc;
                    if (npcDefinition_1.headIcon >= 0 && npcDefinition_1.headIcon < prayerIcon.length) {
                        worldToScreen(((Mobile) (obj)), ((Mobile) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            prayerIcon[npcDefinition_1.headIcon].drawSprite(spriteDrawX - 12, spriteDrawY - 30);
                        }
                    }
                    if (anInt855 == 1 && anInt1222 == localNpcIndices[id - localPlayerCount] && loopCycle % 20 < 10) {
                        worldToScreen(((Mobile) (obj)), ((Mobile) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            hintIcon[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
                        }
                    }
                }
                if (((Mobile) (obj)).textSpoken != null && (id >= localPlayerCount || publicChatMode == 0 || publicChatMode == 3 || publicChatMode == 1 && isFriendOrSelf(((Player) obj).name))) {
                    worldToScreen(((Mobile) (obj)), ((Mobile) (obj)).height);
                    if (spriteDrawX > -1 && anInt974 < anInt975) {
                        anIntArray979[anInt974] = boldFont.getANTextWidth(((Mobile) (obj)).textSpoken) / 2;
                        anIntArray978[anInt974] = boldFont.trimHeight;
                        anIntArray976[anInt974] = spriteDrawX;
                        anIntArray977[anInt974] = spriteDrawY;
                        anIntArray980[anInt974] = ((Mobile) (obj)).textColor;
                        anIntArray981[anInt974] = ((Mobile) (obj)).textEffects;
                        anIntArray982[anInt974] = ((Mobile) (obj)).textCycle;
                        aStringArray983[anInt974++] = ((Mobile) (obj)).textSpoken;
                        if (anInt1249 == 0 && ((Mobile) (obj)).textEffects >= 1 && ((Mobile) (obj)).textEffects <= 3) {
                            anIntArray978[anInt974] += 10;
                            anIntArray977[anInt974] += 5;
                        }
                        if (anInt1249 == 0 && ((Mobile) (obj)).textEffects == 4) {
                            anIntArray979[anInt974] = 60;
                        }
                        if (anInt1249 == 0 && ((Mobile) (obj)).textEffects == 5) {
                            anIntArray978[anInt974] += 5;
                        }
                    }
                }
                if (((Mobile) (obj)).combatCycle > loopCycle) {
                    try {
                        worldToScreen(((Mobile) (obj)), ((Mobile) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            int i1 = (((Mobile) (obj)).currentHealth * 30) / ((Mobile) (obj)).maxHealth;
                            if (i1 > 30) {
                                i1 = 30;
                            }
                            Graphics2D.fillRect(5, spriteDrawY - 3, spriteDrawX - 15, 65280, i1);
                            Graphics2D.fillRect(5, spriteDrawY - 3, (spriteDrawX - 15) + i1, 0xff0000, 30 - i1);
                        }
                    } catch (Exception e) {
                    }
                }
                for (int j1 = 0; j1 < 4; j1++) {
                    if (((Mobile) (obj)).hitCycles[j1] > loopCycle) {
                        worldToScreen(((Mobile) (obj)), ((Mobile) (obj)).height / 2);
                        if (spriteDrawX > -1) {
                            if (j1 == 1) {
                                spriteDrawY -= 20;
                            }
                            if (j1 == 2) {
                                spriteDrawX -= 15;
                                spriteDrawY -= 10;
                            }
                            if (j1 == 3) {
                                spriteDrawX += 15;
                                spriteDrawY -= 10;
                            }
                            hitMarks[((Mobile) (obj)).hitMarkTypes[j1]].drawSprite(spriteDrawX - 12, spriteDrawY - 12);
                            smallFont.drawANCText(0, String.valueOf(((Mobile) (obj)).hitValues[j1]), spriteDrawY + 4, spriteDrawX);
                            smallFont.drawANCText(0xffffff, String.valueOf(((Mobile) (obj)).hitValues[j1]), spriteDrawY + 3, spriteDrawX - 1);
                        }
                    }
                }
            }
            for (int k = 0; k < anInt974; k++) {
                int k1 = anIntArray976[k];
                int l1 = anIntArray977[k];
                int j2 = anIntArray979[k];
                int k2 = anIntArray978[k];
                boolean flag = true;
                while (flag) {
                    flag = false;
                    for (int l2 = 0; l2 < k; l2++) {
                        if (l1 + 2 > anIntArray977[l2] - anIntArray978[l2] && l1 - k2 < anIntArray977[l2] + 2 && k1 - j2 < anIntArray976[l2] + anIntArray979[l2] && k1 + j2 > anIntArray976[l2] - anIntArray979[l2] && anIntArray977[l2] - anIntArray978[l2] < l1) {
                            l1 = anIntArray977[l2] - anIntArray978[l2];
                            flag = true;
                        }
                    }
                }
                spriteDrawX = anIntArray976[k];
                spriteDrawY = anIntArray977[k] = l1;
                String s = aStringArray983[k];
                if (anInt1249 == 0) {
                    int i3 = 0xffff00;
                    if (anIntArray980[k] < 6) {
                        i3 = anIntArray965[anIntArray980[k]];
                    }
                    if (anIntArray980[k] == 6) {
                        i3 = flashEffectCycle % 20 >= 10 ? 0xffff00 : 0xff0000;
                    }
                    if (anIntArray980[k] == 7) {
                        i3 = flashEffectCycle % 20 >= 10 ? 65535 : 255;
                    }
                    if (anIntArray980[k] == 8) {
                        i3 = flashEffectCycle % 20 >= 10 ? 0x80ff80 : 45056;
                    }
                    if (anIntArray980[k] == 9) {
                        int j3 = 150 - anIntArray982[k];
                        if (j3 < 50) {
                            i3 = 0xff0000 + 1280 * j3;
                        } else if (j3 < 100) {
                            i3 = 0xffff00 - 0x50000 * (j3 - 50);
                        } else if (j3 < 150) {
                            i3 = 65280 + 5 * (j3 - 100);
                        }
                    }
                    if (anIntArray980[k] == 10) {
                        int k3 = 150 - anIntArray982[k];
                        if (k3 < 50) {
                            i3 = 0xff0000 + 5 * k3;
                        } else if (k3 < 100) {
                            i3 = 0xff00ff - 0x50000 * (k3 - 50);
                        } else if (k3 < 150) {
                            i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
                        }
                    }
                    if (anIntArray980[k] == 11) {
                        int l3 = 150 - anIntArray982[k];
                        if (l3 < 50) {
                            i3 = 0xffffff - 0x50005 * l3;
                        } else if (l3 < 100) {
                            i3 = 65280 + 0x50005 * (l3 - 50);
                        } else if (l3 < 150) {
                            i3 = 0xffffff - 0x50000 * (l3 - 100);
                        }
                    }
                    if (anIntArray981[k] == 0) {
                        boldFont.drawANCText(0, s, spriteDrawY + 1, spriteDrawX);
                        boldFont.drawANCText(i3, s, spriteDrawY, spriteDrawX);
                    }
                    if (anIntArray981[k] == 1) {
                        boldFont.drawWaveText(0, s, spriteDrawX, flashEffectCycle, spriteDrawY + 1);
                        boldFont.drawWaveText(i3, s, spriteDrawX, flashEffectCycle, spriteDrawY);
                    }
                    if (anIntArray981[k] == 2) {
                        boldFont.drawWave2Text(spriteDrawX, s, flashEffectCycle, spriteDrawY + 1, 0);
                        boldFont.drawWave2Text(spriteDrawX, s, flashEffectCycle, spriteDrawY, i3);
                    }
                    if (anIntArray981[k] == 3) {
                        boldFont.drawShakeText(150 - anIntArray982[k], s, flashEffectCycle, spriteDrawY + 1, spriteDrawX, 0);
                        boldFont.drawShakeText(150 - anIntArray982[k], s, flashEffectCycle, spriteDrawY, spriteDrawX, i3);
                    }
                    if (anIntArray981[k] == 4) {
                        int i4 = boldFont.getANTextWidth(s);
                        int k4 = ((150 - anIntArray982[k]) * (i4 + 100)) / 150;
                        Graphics2D.setBounds(334, spriteDrawX - 50, spriteDrawX + 50, 0);
                        boldFont.drawText(0, s, spriteDrawY + 1, (spriteDrawX + 50) - k4);
                        boldFont.drawText(i3, s, spriteDrawY, (spriteDrawX + 50) - k4);
                        Graphics2D.resetBounds();
                    }
                    if (anIntArray981[k] == 5) {
                        int j4 = 150 - anIntArray982[k];
                        int l4 = 0;
                        if (j4 < 25) {
                            l4 = j4 - 25;
                        } else if (j4 > 125) {
                            l4 = j4 - 125;
                        }
                        Graphics2D.setBounds(spriteDrawY + 5, 0, 512, spriteDrawY - boldFont.trimHeight - 1);
                        boldFont.drawANCText(0, s, spriteDrawY + 1 + l4, spriteDrawX);
                        boldFont.drawANCText(i3, s, spriteDrawY + l4, spriteDrawX);
                        Graphics2D.resetBounds();
                    }
                } else {
                    boldFont.drawANCText(0, s, spriteDrawY + 1, spriteDrawX);
                    boldFont.drawANCText(0xffff00, s, spriteDrawY, spriteDrawX);
                }
            }
        } catch (Exception e) {
        }
    }

    private void delFriend(long friendId) {
        try {
            if (friendId == 0L) {
                return;
            }
            for (int i = 0; i < friendsCount; i++) {
                if (friendsNamesAsLongs[i] != friendId) {
                    continue;
                }
                friendsCount--;
                needDrawTabArea = true;
                for (int j = i; j < friendsCount; j++) {
                    friendsNames[j] = friendsNames[j + 1];
                    friendsWorlds[j] = friendsWorlds[j + 1];
                    friendsNamesAsLongs[j] = friendsNamesAsLongs[j + 1];
                }
                outputStream.writeOpcode(215);
                outputStream.writeLong(friendId);
                break;
            }
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("18622, " + false + ", " + friendId + ", " + runtimeexception.toString());
            throw new RuntimeException();
        }
    }

    private void drawTabArea() {
        aGraphicsBuffer_1163.initDrawingArea();
        Rasterizer.lineOffsets = anIntArray1181;
        invBack.drawIndexedSprite(0, 0);
        if (invOverlayInterfaceID != -1) {
            drawInterface(0, 0, Interface.cachedInterfaces[invOverlayInterfaceID], 0);
        } else if (tabInterfaceIDs[tabID] != -1) {
            drawInterface(0, 0, Interface.cachedInterfaces[tabInterfaceIDs[tabID]], 0);
        }
        if (menuOpen && menuScreenArea == 1) {
            drawMenu();
        }
        aGraphicsBuffer_1163.drawGraphics(205, super.graphics, 553);
        gameScreenCanvas.initDrawingArea();
        Rasterizer.lineOffsets = anIntArray1182;
    }

    /**
     * It looks like this method renders textures.
     * Putting this here as a placeholder as I'm in a rush.
     */
    private void method37(int arg0) {
        if (!Client.lowMem) {
            if (Rasterizer.texturePriorities[17] >= arg0) {
                IndexedSprite indexedSprite = Rasterizer.textures[17];
                int k = indexedSprite.width * indexedSprite.height - 1;
                int j1 = indexedSprite.width * anInt945 * 2;
                byte abyte0[] = indexedSprite.pixels;
                byte abyte3[] = aByteArray912;
                for (int i2 = 0; i2 <= k; i2++) {
                    abyte3[i2] = abyte0[i2 - j1 & k];
                }
                indexedSprite.pixels = abyte3;
                aByteArray912 = abyte0;
                Rasterizer.resetTexture(17);
                Client.anInt854++;
                if (Client.anInt854 > 1235) {
                    Client.anInt854 = 0;
                    outputStream.writeOpcode(226);
                    outputStream.writeByte(0);
                    int l2 = outputStream.offset;
                    outputStream.writeShort(58722);
                    outputStream.writeByte(240);
                    outputStream.writeShort((int) (Math.random() * 65536D));
                    outputStream.writeByte((int) (Math.random() * 256D));
                    if ((int) (Math.random() * 2D) == 0) {
                        outputStream.writeShort(51825);
                    }
                    outputStream.writeByte((int) (Math.random() * 256D));
                    outputStream.writeShort((int) (Math.random() * 65536D));
                    outputStream.writeShort(7130);
                    outputStream.writeShort((int) (Math.random() * 65536D));
                    outputStream.writeShort(61657);
                    outputStream.writeSizeByte(outputStream.offset - l2);
                }
            }
            if (Rasterizer.texturePriorities[24] >= arg0) {
                IndexedSprite indexedSprite_1 = Rasterizer.textures[24];
                int l = indexedSprite_1.width * indexedSprite_1.height - 1;
                int k1 = indexedSprite_1.width * anInt945 * 2;
                byte abyte1[] = indexedSprite_1.pixels;
                byte abyte4[] = aByteArray912;
                for (int j2 = 0; j2 <= l; j2++) {
                    abyte4[j2] = abyte1[j2 - k1 & l];
                }
                indexedSprite_1.pixels = abyte4;
                aByteArray912 = abyte1;
                Rasterizer.resetTexture(24);
            }
            if (Rasterizer.texturePriorities[34] >= arg0) {
                IndexedSprite indexedSprite_2 = Rasterizer.textures[34];
                int i1 = indexedSprite_2.width * indexedSprite_2.height - 1;
                int l1 = indexedSprite_2.width * anInt945 * 2;
                byte abyte2[] = indexedSprite_2.pixels;
                byte abyte5[] = aByteArray912;
                for (int k2 = 0; k2 <= i1; k2++) {
                    abyte5[k2] = abyte2[k2 - l1 & i1];
                }
                indexedSprite_2.pixels = abyte5;
                aByteArray912 = abyte2;
                Rasterizer.resetTexture(34);
            }
        }
    }

    private void method38() {
        for (int i = -1; i < localPlayerCount; i++) {
            int j;
            if (i == -1) {
                j = myPlayerIndex;
            } else {
                j = playerIndices[i];
            }
            Player player = localPlayers[j];
            if (player != null && player.textCycle > 0) {
                player.textCycle--;
                if (player.textCycle == 0) {
                    player.textSpoken = null;
                }
            }
        }
        for (int k = 0; k < localNpcCount; k++) {
            int l = localNpcIndices[k];
            Npc npc = localNpcs[l];
            if (npc != null && npc.textCycle > 0) {
                npc.textCycle--;
                if (npc.textCycle == 0) {
                    npc.textSpoken = null;
                }
            }
        }
    }

    private void calcCameraPos() {
        int i = anInt1098 * 128 + 64;
        int j = anInt1099 * 128 + 64;
        int k = method42(plane, j, i) - anInt1100;
        if (xCameraPos < i) {
            xCameraPos += anInt1101 + (i - xCameraPos) * anInt1102 / 1000;
            if (xCameraPos > i) {
                xCameraPos = i;
            }
        }
        if (xCameraPos > i) {
            xCameraPos -= anInt1101 + (xCameraPos - i) * anInt1102 / 1000;
            if (xCameraPos < i) {
                xCameraPos = i;
            }
        }
        if (zCameraPos < k) {
            zCameraPos += anInt1101 + (k - zCameraPos) * anInt1102 / 1000;
            if (zCameraPos > k) {
                zCameraPos = k;
            }
        }
        if (zCameraPos > k) {
            zCameraPos -= anInt1101 + (zCameraPos - k) * anInt1102 / 1000;
            if (zCameraPos < k) {
                zCameraPos = k;
            }
        }
        if (yCameraPos < j) {
            yCameraPos += anInt1101 + (j - yCameraPos) * anInt1102 / 1000;
            if (yCameraPos > j) {
                yCameraPos = j;
            }
        }
        if (yCameraPos > j) {
            yCameraPos -= anInt1101 + (yCameraPos - j) * anInt1102 / 1000;
            if (yCameraPos < j) {
                yCameraPos = j;
            }
        }
        i = anInt995 * 128 + 64;
        j = anInt996 * 128 + 64;
        k = method42(plane, j, i) - anInt997;
        int l = i - xCameraPos;
        int i1 = k - zCameraPos;
        int j1 = j - yCameraPos;
        int k1 = (int) Math.sqrt(l * l + j1 * j1);
        int l1 = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
        int i2 = (int) (Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
        if (l1 < 128) {
            l1 = 128;
        }
        if (l1 > 383) {
            l1 = 383;
        }
        if (yCameraCurve < l1) {
            yCameraCurve += anInt998 + (l1 - yCameraCurve) * anInt999 / 1000;
            if (yCameraCurve > l1) {
                yCameraCurve = l1;
            }
        }
        if (yCameraCurve > l1) {
            yCameraCurve -= anInt998 + (yCameraCurve - l1) * anInt999 / 1000;
            if (yCameraCurve < l1) {
                yCameraCurve = l1;
            }
        }
        int j2 = i2 - xCameraCurve;
        if (j2 > 1024) {
            j2 -= 2048;
        }
        if (j2 < -1024) {
            j2 += 2048;
        }
        if (j2 > 0) {
            xCameraCurve += anInt998 + j2 * anInt999 / 1000;
            xCameraCurve &= 0x7ff;
        }
        if (j2 < 0) {
            xCameraCurve -= anInt998 + -j2 * anInt999 / 1000;
            xCameraCurve &= 0x7ff;
        }
        int k2 = i2 - xCameraCurve;
        if (k2 > 1024) {
            k2 -= 2048;
        }
        if (k2 < -1024) {
            k2 += 2048;
        }
        if (k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0) {
            xCameraCurve = i2;
        }
    }

    private void drawMenu() {
        int i = menuOffsetX;
        int j = menuOffsetY;
        int k = menuWidth;
        int l = anInt952;
        int i1 = 0x5d5447;
        Graphics2D.fillRect(l, j, i, i1, k);
        Graphics2D.fillRect(16, j + 1, i + 1, 0, k - 2);
        Graphics2D.drawRect(i + 1, k - 2, l - 19, 0, j + 18);
        boldFont.drawText(i1, "Choose Option", j + 14, i + 3);
        int j1 = super.mouseX;
        int k1 = super.mouseY;
        if (menuScreenArea == 0) {
            j1 -= 4;
            k1 -= 4;
        }
        if (menuScreenArea == 1) {
            j1 -= 553;
            k1 -= 205;
        }
        if (menuScreenArea == 2) {
            j1 -= 17;
            k1 -= 357;
        }
        for (int l1 = 0; l1 < menuActionCount; l1++) {
            int i2 = j + 31 + (menuActionCount - 1 - l1) * 15;
            int j2 = 0xffffff;
            if (j1 > i && j1 < i + k && k1 > i2 - 13 && k1 < i2 + 3) {
                j2 = 0xffff00;
            }
            boldFont.drawText(true, i + 3, j2, menuActionName[l1], i2);
        }
    }

    private void addFriend(long l) {
        try {
            if (l == 0L) {
                return;
            }
            if (friendsCount >= 100 && anInt1046 != 1) {
                pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
                return;
            }
            if (friendsCount >= 200) {
                pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
                return;
            }
            String s = TextUtil.formatName(TextUtil.longToName(l));
            for (int i = 0; i < friendsCount; i++) {
                if (friendsNamesAsLongs[i] == l) {
                    pushMessage(s + " is already on your friend list", 0, "");
                    return;
                }
            }
            for (int j = 0; j < ignoreCount; j++) {
                if (ignoreListAsLongs[j] == l) {
                    pushMessage("Please remove " + s + " from your ignore list first", 0, "");
                    return;
                }
            }
            if (s.equals(Client.myPlayer.name)) {
                return;
            } else {
                friendsNames[friendsCount] = s;
                friendsNamesAsLongs[friendsCount] = l;
                friendsWorlds[friendsCount] = 0;
                friendsCount++;
                needDrawTabArea = true;
                outputStream.writeOpcode(188);
                outputStream.writeLong(l);
                return;
            }
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("15283, " + (byte) 68 + ", " + l + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private int method42(int arg0, int arg1, int arg2) {
        int l = arg2 >> 7;
        int i1 = arg1 >> 7;
        if (l < 0 || i1 < 0 || l > 103 || i1 > 103) {
            return 0;
        }
        int j1 = arg0;
        if (j1 < 3 && (byteGroundArray[1][l][i1] & 2) == 2) {
            j1++;
        }
        int k1 = arg2 & 0x7f;
        int l1 = arg1 & 0x7f;
        int i2 = intGroundArray[j1][l][i1] * (128 - k1) + intGroundArray[j1][l + 1][i1] * k1 >> 7;
        int j2 = intGroundArray[j1][l][i1 + 1] * (128 - k1) + intGroundArray[j1][l + 1][i1 + 1] * k1 >> 7;
        return i2 * (128 - l1) + j2 * l1 >> 7;
    }

    private static String getFormattedValue(int value) { // intToKOrMil
        if (value < 100000) {
            return String.valueOf(value);
        }
        if (value < 10000000) {
            return value / 1000 + "K";
        } else {
            return value / 1000000 + "M";
        }
    }

    private void processLogout() { // resetLogout
        try {
            if (socketStream != null) {
                socketStream.close();
            }
        } catch (Exception exception) {
        }
        socketStream = null;
        loggedIn = false;
        loginScreenState = 0;
        // myUsername = "";
        // myPassword = "";
        clearCache();
        sceneGraph.reset();
        for (int i = 0; i < 4; i++) {
            collisionMaps[i].reset();
        }
        System.gc();
        stopMidi();
        currentSong = -1;
        nextSong = -1;
        prevSong = 0;
    }

    private void method45() {
        aBoolean1031 = true;
        for (int j = 0; j < 7; j++) {
            anIntArray1065[j] = -1;
            for (int k = 0; k < IdentityKit.length; k++) {
                if (IdentityKit.designCache[k].aBoolean662 || IdentityKit.designCache[k].anInt657 != j + (aBoolean1047 ? 0 : 7)) {
                    continue;
                }
                anIntArray1065[j] = k;
                break;
            }
        }
    }

    private void method46(int arg0, Stream stream) {
        while (stream.bitOffset + 21 < arg0 * 8) {
            int k = stream.getBits(14);
            if (k == 16383) {
                break;
            }
            if (localNpcs[k] == null) {
                localNpcs[k] = new Npc();
            }
            Npc npc = localNpcs[k];
            localNpcIndices[localNpcCount++] = k;
            npc.lastUpdate = Client.loopCycle;
            int l = stream.getBits(5);
            if (l > 15) {
                l -= 32;
            }
            int i1 = stream.getBits(5);
            if (i1 > 15) {
                i1 -= 32;
            }
            int j1 = stream.getBits(1);
            npc.desc = NpcDefinition.forId(stream.getBits(12));
            int k1 = stream.getBits(1);
            if (k1 == 1) {
                playerUpdateIndices[entityUpdateCount++] = k;
            }
            npc.anInt1540 = npc.desc.size;
            npc.defaultTurnValue = npc.desc.turnAmount;
            npc.walkAnimation = npc.desc.walkAnim;
            npc.anInt1555 = npc.desc.retreatAnim;
            npc.anInt1556 = npc.desc.turnRightAnim;
            npc.anInt1557 = npc.desc.turnLeftAnim;
            npc.idleAnimation = npc.desc.idleAnim;
            npc.updatePosition(Client.myPlayer.walkQueueX[0] + i1, Client.myPlayer.walkQueueY[0] + l, j1 == 1);
        }
        stream.endBitBlock();
    }

    public void processGameLoop() {
        if (alreadyLoaded || loadingError || genericLoadingError) {
            return;
        }
        Client.loopCycle++;
        if (!loggedIn) {
            processLoginScreenInput();
        } else {
            mainGameProcessor();
        }
        processOnDemandQueue();
    }

    private void renderPlayers(boolean flag) {
        if (Client.myPlayer.x >> 7 == destX && Client.myPlayer.y >> 7 == destY) {
            destX = 0;
        }
        int j = localPlayerCount;
        if (flag) {
            j = 1;
        }
        for (int l = 0; l < j; l++) {
            Player player;
            int i1;
            if (flag) {
                player = Client.myPlayer;
                i1 = myPlayerIndex << 14;
            } else {
                player = localPlayers[playerIndices[l]];
                i1 = playerIndices[l] << 14;
            }
            if (player == null || !player.isVisible()) {
                continue;
            }
            player.aBoolean1699 = (Client.lowMem && localPlayerCount > 50 || localPlayerCount > 200) && !flag && player.anInt1517 == player.idleAnimation;
            int j1 = player.x >> 7;
            int k1 = player.y >> 7;
            if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104) {
                continue;
            }
            if (player.aModel_1714 != null && Client.loopCycle >= player.anInt1707 && Client.loopCycle < player.anInt1708) {
                player.aBoolean1699 = false;
                player.anInt1709 = method42(plane, player.y, player.x);
                sceneGraph.method286(plane, player.y, player, player.anInt1552, player.anInt1722, player.x, player.anInt1709, player.anInt1719, player.anInt1721, i1, player.anInt1720);
                continue;
            }
            if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
                if (anIntArrayArray929[j1][k1] == flashEffectCycle) {
                    continue;
                }
                anIntArrayArray929[j1][k1] = flashEffectCycle;
            }
            player.anInt1709 = method42(plane, player.y, player.x);
            sceneGraph.method285(plane, player.anInt1552, player.anInt1709, i1, player.y, 60, player.x, player, player.aBoolean1541);
        }
    }

    private boolean promptUserForInput(Interface component) {
        int id = component.contentType;
        if (anInt900 == 2) {
            if (id == 201) {
                inputTaken = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 1;
                aString1121 = "Enter name of friend to add to list";
            }
            if (id == 202) {
                inputTaken = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 2;
                aString1121 = "Enter name of friend to delete from list";
            }
        }
        if (id == 205) {
            anInt1011 = 250;
            return true;
        }
        if (id == 501) {
            inputTaken = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 4;
            aString1121 = "Enter name of player to add to list";
        }
        if (id == 502) {
            inputTaken = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 5;
            aString1121 = "Enter name of player to delete from list";
        }
        if (id >= 300 && id <= 313) {
            int k = (id - 300) / 2;
            int j1 = id & 1;
            int i2 = anIntArray1065[k];
            if (i2 != -1) {
                do {
                    if (j1 == 0 && --i2 < 0) {
                        i2 = IdentityKit.length - 1;
                    }
                    if (j1 == 1 && ++i2 >= IdentityKit.length) {
                        i2 = 0;
                    }
                } while (IdentityKit.designCache[i2].aBoolean662 || IdentityKit.designCache[i2].anInt657 != k + (aBoolean1047 ? 0 : 7));
                anIntArray1065[k] = i2;
                aBoolean1031 = true;
            }
        }
        if (id >= 314 && id <= 323) {
            int l = (id - 314) / 2;
            int k1 = id & 1;
            int j2 = anIntArray990[l];
            if (k1 == 0 && --j2 < 0) {
                j2 = Client.anIntArrayArray1003[l].length - 1;
            }
            if (k1 == 1 && ++j2 >= Client.anIntArrayArray1003[l].length) {
                j2 = 0;
            }
            anIntArray990[l] = j2;
            aBoolean1031 = true;
        }
        if (id == 324 && !aBoolean1047) {
            aBoolean1047 = true;
            method45();
        }
        if (id == 325 && aBoolean1047) {
            aBoolean1047 = false;
            method45();
        }
        if (id == 326) {
            outputStream.writeOpcode(101);
            outputStream.writeByte(aBoolean1047 ? 0 : 1);
            for (int i1 = 0; i1 < 7; i1++) {
                outputStream.writeByte(anIntArray1065[i1]);
            }
            for (int l1 = 0; l1 < 5; l1++) {
                outputStream.writeByte(anIntArray990[l1]);
            }
            return true;
        }
        if (id == 613) {
            canMute = !canMute;
        }
        if (id >= 601 && id <= 612) {
            closeOpenInterfaces();
            if (reportAbuseInput.length() > 0) {
                outputStream.writeOpcode(218);
                outputStream.writeLong(TextUtil.nameToLong(reportAbuseInput));
                outputStream.writeByte(id - 601);
                outputStream.writeByte(canMute ? 1 : 0);
            }
        }
        return false;
    }

    private void parsePlayerUpdateFlags(Stream stream) {
        for (int j = 0; j < entityUpdateCount; j++) {
            int k = playerUpdateIndices[j];
            Player player = localPlayers[k];
            int l = stream.getUnsignedByte();
            if ((l & 0x40) != 0) {
                l += stream.getUnsignedByte() << 8;
            }
            parsePlayerUpdateFlag(l, k, stream, player);
        }
    }

    private void method50(int arg0, int arg1, int arg3, int arg4, int arg5) {
        int k1 = sceneGraph.getWallObjectUid(arg5, arg3, arg0);
        if (k1 != 0) {
            int l1 = sceneGraph.method304(arg5, arg3, arg0, k1);
            int k2 = l1 >> 6 & 3;
            int i3 = l1 & 0x1f;
            int k3 = arg1;
            if (k1 > 0) {
                k3 = arg4;
            }
            int ai[] = minimap.pixels;
            int k4 = 24624 + arg3 * 4 + (103 - arg0) * 512 * 4;
            int i5 = k1 >> 14 & 0x7fff;
            ObjectDefinition objectDefinition_2 = ObjectDefinition.forId(i5);
            if (objectDefinition_2.mapSceneId != -1) {
                IndexedSprite indexedSprite_2 = mapScenes[objectDefinition_2.mapSceneId];
                if (indexedSprite_2 != null) {
                    int i6 = (objectDefinition_2.ySize * 4 - indexedSprite_2.width) / 2;
                    int j6 = (objectDefinition_2.xSize * 4 - indexedSprite_2.height) / 2;
                    indexedSprite_2.drawIndexedSprite(48 + arg3 * 4 + i6, 48 + (104 - arg0 - objectDefinition_2.xSize) * 4 + j6);
                }
            } else {
                if (i3 == 0 || i3 == 2) {
                    if (k2 == 0) {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else if (k2 == 1) {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 3) {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
                }
                if (i3 == 3) {
                    if (k2 == 0) {
                        ai[k4] = k3;
                    } else if (k2 == 1) {
                        ai[k4 + 3] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 3) {
                        ai[k4 + 1536] = k3;
                    }
                }
                if (i3 == 2) {
                    if (k2 == 3) {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else if (k2 == 0) {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else if (k2 == 1) {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
                }
            }
        }
        k1 = sceneGraph.getInteractiveObjectUid(arg5, arg3, arg0);
        if (k1 != 0) {
            int i2 = sceneGraph.method304(arg5, arg3, arg0, k1);
            int l2 = i2 >> 6 & 3;
            int j3 = i2 & 0x1f;
            int l3 = k1 >> 14 & 0x7fff;
            ObjectDefinition objectDefinition_1 = ObjectDefinition.forId(l3);
            if (objectDefinition_1.mapSceneId != -1) {
                IndexedSprite indexedSprite_1 = mapScenes[objectDefinition_1.mapSceneId];
                if (indexedSprite_1 != null) {
                    int j5 = (objectDefinition_1.ySize * 4 - indexedSprite_1.width) / 2;
                    int k5 = (objectDefinition_1.xSize * 4 - indexedSprite_1.height) / 2;
                    indexedSprite_1.drawIndexedSprite(48 + arg3 * 4 + j5, 48 + (104 - arg0 - objectDefinition_1.xSize) * 4 + k5);
                }
            } else if (j3 == 9) {
                int l4 = 0xeeeeee;
                if (k1 > 0) {
                    l4 = 0xee0000;
                }
                int ai1[] = minimap.pixels;
                int l5 = 24624 + arg3 * 4 + (103 - arg0) * 512 * 4;
                if (l2 == 0 || l2 == 2) {
                    ai1[l5 + 1536] = l4;
                    ai1[l5 + 1024 + 1] = l4;
                    ai1[l5 + 512 + 2] = l4;
                    ai1[l5 + 3] = l4;
                } else {
                    ai1[l5] = l4;
                    ai1[l5 + 512 + 1] = l4;
                    ai1[l5 + 1024 + 2] = l4;
                    ai1[l5 + 1536 + 3] = l4;
                }
            }
        }
        k1 = sceneGraph.getGroundDecorationUid(arg5, arg3, arg0);
        if (k1 != 0) {
            int j2 = k1 >> 14 & 0x7fff;
            ObjectDefinition objectDefinition = ObjectDefinition.forId(j2);
            if (objectDefinition.mapSceneId != -1) {
                IndexedSprite indexedSprite = mapScenes[objectDefinition.mapSceneId];
                if (indexedSprite != null) {
                    int i4 = (objectDefinition.ySize * 4 - indexedSprite.width) / 2;
                    int j4 = (objectDefinition.xSize * 4 - indexedSprite.height) / 2;
                    indexedSprite.drawIndexedSprite(48 + arg3 * 4 + i4, 48 + (104 - arg0 - objectDefinition.xSize) * 4 + j4);
                }
            }
        }
    }

    private void loadTitleScreen() {
        titleBox = new IndexedSprite(titleArchive, "titlebox", 0);
        titleButton = new IndexedSprite(titleArchive, "titlebutton", 0);
        runes = new IndexedSprite[12];
        int j = 0;
        try {
            j = Integer.parseInt(getParameter("fl_icon"));
        } catch (Exception exception) {
        }
        if (j == 0) {
            for (int k = 0; k < 12; k++) {
                runes[k] = new IndexedSprite(titleArchive, "runes", k);
            }
        } else {
            for (int l = 0; l < 12; l++) {
                runes[l] = new IndexedSprite(titleArchive, "runes", 12 + (l & 3));
            }
        }
        aClass30_Sub2_Sub1_Sub1_1201 = new Sprite(128, 265);
        aClass30_Sub2_Sub1_Sub1_1202 = new Sprite(128, 265);
        System.arraycopy(leftFlameCanvas.componentPixels, 0, aClass30_Sub2_Sub1_Sub1_1201.pixels, 0, 33920);
        System.arraycopy(rightFlameCanvas.componentPixels, 0, aClass30_Sub2_Sub1_Sub1_1202.pixels, 0, 33920);
        anIntArray851 = new int[256];
        for (int k1 = 0; k1 < 64; k1++) {
            anIntArray851[k1] = k1 * 0x40000;
        }
        for (int l1 = 0; l1 < 64; l1++) {
            anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1;
        }
        for (int i2 = 0; i2 < 64; i2++) {
            anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;
        }
        for (int j2 = 0; j2 < 64; j2++) {
            anIntArray851[j2 + 192] = 0xffffff;
        }
        anIntArray852 = new int[256];
        for (int k2 = 0; k2 < 64; k2++) {
            anIntArray852[k2] = k2 * 1024;
        }
        for (int l2 = 0; l2 < 64; l2++) {
            anIntArray852[l2 + 64] = 65280 + 4 * l2;
        }
        for (int i3 = 0; i3 < 64; i3++) {
            anIntArray852[i3 + 128] = 65535 + 0x40000 * i3;
        }
        for (int j3 = 0; j3 < 64; j3++) {
            anIntArray852[j3 + 192] = 0xffffff;
        }
        anIntArray853 = new int[256];
        for (int k3 = 0; k3 < 64; k3++) {
            anIntArray853[k3] = k3 * 4;
        }
        for (int l3 = 0; l3 < 64; l3++) {
            anIntArray853[l3 + 64] = 255 + 0x40000 * l3;
        }
        for (int i4 = 0; i4 < 64; i4++) {
            anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4;
        }
        for (int j4 = 0; j4 < 64; j4++) {
            anIntArray853[j4 + 192] = 0xffffff;
        }
        anIntArray850 = new int[256];
        anIntArray1190 = new int[32768];
        anIntArray1191 = new int[32768];
        randomizeIndexedSprite(null);
        anIntArray828 = new int[32768];
        anIntArray829 = new int[32768];
        drawLoadingText(10, "Connecting to fileserver");
        if (!aBoolean831) {
            drawFlames = true;
            aBoolean831 = true;
            startRunnable(this, 2);
        }
    }

    private static void setHighMem() {
        SceneGraph.lowMem = false;
        Rasterizer.lowMem = false;
        Client.lowMem = false;
        Region.lowMem = false;
        ObjectDefinition.lowMem = false;
    }

    public static void main(String args[]) {
        try {
            System.out.println("RS2 user client - release #" + 317);
            if (args.length != 5) {
                System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
                return;
            }
            Client.nodeID = Integer.parseInt(args[0]);
            Client.portOff = Integer.parseInt(args[1]);
            if (args[2].equals("lowmem")) {
                Client.setLowMem();
            } else if (args[2].equals("highmem")) {
                Client.setHighMem();
            } else {
                System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
                return;
            }
            if (args[3].equals("free")) {
                Client.isMembers = false;
            } else if (args[3].equals("members")) {
                Client.isMembers = true;
            } else {
                System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
                return;
            }
            Signlink.storeid = Integer.parseInt(args[4]);
            Signlink.startpriv(InetAddress.getLocalHost());
            Client client = new Client();
            client.initGameFrame(503, 765);
        } catch (Exception exception) {
        }
    }

    private void processLoadState() { // loadingStages
        if (Client.lowMem && loadingStage == 2 && Region.anInt131 != plane) {
            gameScreenCanvas.initDrawingArea();
            aFont_1271.drawANCText(0, "Loading - please wait.", 151, 257);
            aFont_1271.drawANCText(0xffffff, "Loading - please wait.", 150, 256);
            gameScreenCanvas.drawGraphics(4, super.graphics, 4);
            loadingStage = 1;
            aLong824 = System.currentTimeMillis();
        }
        if (loadingStage == 1) {
            int j = method54();
            if (j != 0 && System.currentTimeMillis() - aLong824 > 0x57e40L) {
                Signlink.reporterror(myUsername + " glcfb " + aLong1215 + "," + j + "," + Client.lowMem + "," + cacheIndices[0] + "," + onDemandFetcher.getRemaining() + "," + plane + "," + anInt1069 + "," + anInt1070);
                aLong824 = System.currentTimeMillis();
            }
        }
        if (loadingStage == 2 && plane != anInt985) {
            anInt985 = plane;
            method24(plane);
        }
    }

    private int method54() {
        for (int i = 0; i < aByteArrayArray1183.length; i++) {
            if (aByteArrayArray1183[i] == null && anIntArray1235[i] != -1) {
                return -1;
            }
            if (aByteArrayArray1247[i] == null && anIntArray1236[i] != -1) {
                return -2;
            }
        }
        boolean flag = true;
        for (int j = 0; j < aByteArrayArray1183.length; j++) {
            byte abyte0[] = aByteArrayArray1247[j];
            if (abyte0 != null) {
                int k = (anIntArray1234[j] >> 8) * 64 - baseX;
                int l = (anIntArray1234[j] & 0xff) * 64 - baseY;
                if (aBoolean1159) {
                    k = 10;
                    l = 10;
                }
                flag &= Region.method189(k, abyte0, l);
            }
        }
        if (!flag) {
            return -3;
        }
        if (aBoolean1080) {
            return -4;
        } else {
            loadingStage = 2;
            Region.anInt131 = plane;
            method22();
            outputStream.writeOpcode(121);
            return 0;
        }
    }

    private void renderProjectiles() {
        for (Projectile projectile = (Projectile) projectileList.getFront(); projectile != null; projectile = (Projectile) projectileList.getNext()) {
            if (projectile.plane != plane || Client.loopCycle > projectile.speed) {
                projectile.unlink();
            } else if (Client.loopCycle >= projectile.time) {
                if (projectile.target > 0) {
                    Npc npc = localNpcs[projectile.target - 1];
                    if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312) {
                        projectile.method455(Client.loopCycle, npc.y, method42(projectile.plane, npc.y, npc.x) - projectile.endHeight, npc.x);
                    }
                }
                if (projectile.target < 0) {
                    int j = -projectile.target - 1;
                    Player player;
                    if (j == unknownInt10) {
                        player = Client.myPlayer;
                    } else {
                        player = localPlayers[j];
                    }
                    if (player != null && player.x >= 0 && player.x < 13312 && player.y >= 0 && player.y < 13312) {
                        projectile.method455(Client.loopCycle, player.y, method42(projectile.plane, player.y, player.x) - projectile.endHeight, player.x);
                    }
                }
                projectile.method456(anInt945);
                sceneGraph.method285(plane, projectile.anInt1595, (int) projectile.aDouble1587, -1, (int) projectile.aDouble1586, 60, (int) projectile.aDouble1585, projectile, false);
            }
        }
    }

    public AppletContext getAppletContext() {
        if (Signlink.mainapp != null) {
            return Signlink.mainapp.getAppletContext();
        } else {
            return super.getAppletContext();
        }
    }

    private void drawTitleScreen() { // drawLogo
        byte buf[] = titleArchive.get("title.dat");
        Sprite sprite = new Sprite(buf, this);
        leftFlameCanvas.initDrawingArea();
        sprite.drawFlippedSprite(0, 0);
        rightFlameCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-637, 0);
        topLeftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-128, 0);
        bottomLeftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-202, -371);
        leftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-202, -171);
        titleBoxLeftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(0, -265);
        bottomRightCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-562, -265);
        smallLeftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-128, -171);
        smallRightCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-562, -171);
        int src[] = new int[sprite.width];
        for (int height = 0; height < sprite.height; height++) {
            for (int width = 0; width < sprite.width; width++) {
                src[width] = sprite.pixels[sprite.width - width - 1 + sprite.width * height];
            }
            System.arraycopy(src, 0, sprite.pixels, sprite.width * height, sprite.width);
        }
        leftFlameCanvas.initDrawingArea();
        sprite.drawFlippedSprite(382, 0);
        rightFlameCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-255, 0);
        topLeftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(254, 0);
        bottomLeftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(180, -371);
        leftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(180, -171);
        titleBoxLeftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(382, -265);
        bottomRightCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-180, -265);
        smallLeftCanvas.initDrawingArea();
        sprite.drawFlippedSprite(254, -171);
        smallRightCanvas.initDrawingArea();
        sprite.drawFlippedSprite(-180, -171);
        sprite = new Sprite(titleArchive, "logo", 0);
        topLeftCanvas.initDrawingArea();
        sprite.drawSprite(382 - sprite.width / 2 - 128, 18);
        sprite = null;
        System.gc();
    }

    private void processOnDemandQueue() {
        do {
            OnDemandRequest onDemandRequest;
            do {
                onDemandRequest = onDemandFetcher.getNextNode();
                if (onDemandRequest == null) {
                    return;
                }
                if (onDemandRequest.dataType == 0) {
                    Model.formatTriangles(onDemandRequest.buffer, onDemandRequest.id);
                    if ((onDemandFetcher.getModelIndex(onDemandRequest.id) & 0x62) != 0) {
                        needDrawTabArea = true;
                        if (backDialogID != -1) {
                            inputTaken = true;
                        }
                    }
                }
                if (onDemandRequest.dataType == 1 && onDemandRequest.buffer != null) {
                    Animation.method529(onDemandRequest.buffer);
                }
                if (onDemandRequest.dataType == 2 && onDemandRequest.id == nextSong && onDemandRequest.buffer != null) {
                    saveMidi(songChanging, onDemandRequest.buffer);
                }
                if (onDemandRequest.dataType == 3 && loadingStage == 1) {
                    for (int i = 0; i < aByteArrayArray1183.length; i++) {
                        if (anIntArray1235[i] == onDemandRequest.id) {
                            aByteArrayArray1183[i] = onDemandRequest.buffer;
                            if (onDemandRequest.buffer == null) {
                                anIntArray1235[i] = -1;
                            }
                            break;
                        }
                        if (anIntArray1236[i] != onDemandRequest.id) {
                            continue;
                        }
                        aByteArrayArray1247[i] = onDemandRequest.buffer;
                        if (onDemandRequest.buffer == null) {
                            anIntArray1236[i] = -1;
                        }
                        break;
                    }
                }
            } while (onDemandRequest.dataType != 93 || !onDemandFetcher.method564(onDemandRequest.id));
            Region.method173(new Stream(onDemandRequest.buffer), onDemandFetcher);
        } while (true);
    }

    private void getFlameOffsets() { // calcFlamesPosition
        char c = '\u0100';
        for (int j = 10; j < 117; j++) {
            int k = (int) (Math.random() * 100D);
            if (k < 50) {
                anIntArray828[j + (c - 2 << 7)] = 255;
            }
        }
        for (int l = 0; l < 100; l++) {
            int i1 = (int) (Math.random() * 124D) + 2;
            int k1 = (int) (Math.random() * 128D) + 128;
            int k2 = i1 + (k1 << 7);
            anIntArray828[k2] = 192;
        }
        for (int j1 = 1; j1 < c - 1; j1++) {
            for (int l1 = 1; l1 < 127; l1++) {
                int l2 = l1 + (j1 << 7);
                anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128] + anIntArray828[l2 + 128]) / 4;
            }
        }
        anInt1275 += 128;
        if (anInt1275 > anIntArray1190.length) {
            anInt1275 -= anIntArray1190.length;
            int i2 = (int) (Math.random() * 12D);
            randomizeIndexedSprite(runes[i2]);
        }
        for (int j2 = 1; j2 < c - 1; j2++) {
            for (int i3 = 1; i3 < 127; i3++) {
                int k3 = i3 + (j2 << 7);
                int i4 = anIntArray829[k3 + 128] - anIntArray1190[k3 + anInt1275 & anIntArray1190.length - 1] / 5;
                if (i4 < 0) {
                    i4 = 0;
                }
                anIntArray828[k3] = i4;
            }
        }
        System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);
        anIntArray969[c - 1] = (int) (Math.sin((double) Client.loopCycle / 14D) * 16D + Math.sin((double) Client.loopCycle / 15D) * 14D + Math.sin((double) Client.loopCycle / 16D) * 12D);
        if (anInt1040 > 0) {
            anInt1040 -= 4;
        }
        if (anInt1041 > 0) {
            anInt1041 -= 4;
        }
        if (anInt1040 == 0 && anInt1041 == 0) {
            int l3 = (int) (Math.random() * 2000D);
            if (l3 == 0) {
                anInt1040 = 1024;
            }
            if (l3 == 1) {
                anInt1041 = 1024;
            }
        }
    }

    private boolean saveWave(byte buf[], int id) {
        return buf == null || Signlink.wavesave(buf, id);
    }

    private void method60(int id) {
        Interface interfaceInstance = Interface.cachedInterfaces[id];
        for (int childId : interfaceInstance.children) {
            if (childId == -1) {
                break;
            }
            Interface children = Interface.cachedInterfaces[childId];
            if (children.interfaceType == 1) {
                method60(children.id);
            }
            children.anInt246 = 0;
            children.anInt208 = 0;
        }
    }

    private void drawHeadIcon() {
        if (anInt855 != 2) {
            return;
        }
        getEntityScreenPos((anInt934 - baseX << 7) + anInt937, anInt936 * 2, (anInt935 - baseY << 7) + anInt938);
        if (spriteDrawX > -1 && Client.loopCycle % 20 < 10) {
            hintIcon[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
        }
    }

    private void mainGameProcessor() {
        if (systemUpdateTime > 1) {
            systemUpdateTime--;
        }
        if (anInt1011 > 0) {
            anInt1011--;
        }
        for (int j = 0; j < 5; j++) {
            if (!parsePacket()) {
                break;
            }
        }
        if (!loggedIn) {
            return;
        }
        synchronized (mouseRecorder.lock) {
            if (Client.flagged) {
                if (super.clickMode3 != 0 || mouseRecorder.cacheIndex >= 40) {
                    outputStream.writeOpcode(45);
                    outputStream.writeByte(0);
                    int j2 = outputStream.offset;
                    int j3 = 0;
                    for (int j4 = 0; j4 < mouseRecorder.cacheIndex; j4++) {
                        if (j2 - outputStream.offset >= 240) {
                            break;
                        }
                        j3++;
                        int l4 = mouseRecorder.mouseYCache[j4];
                        if (l4 < 0) {
                            l4 = 0;
                        } else if (l4 > 502) {
                            l4 = 502;
                        }
                        int k5 = mouseRecorder.mouseXCache[j4];
                        if (k5 < 0) {
                            k5 = 0;
                        } else if (k5 > 764) {
                            k5 = 764;
                        }
                        int i6 = l4 * 765 + k5;
                        if (mouseRecorder.mouseYCache[j4] == -1 && mouseRecorder.mouseXCache[j4] == -1) {
                            k5 = -1;
                            l4 = -1;
                            i6 = 0x7ffff;
                        }
                        if (k5 == anInt1237 && l4 == anInt1238) {
                            if (anInt1022 < 2047) {
                                anInt1022++;
                            }
                        } else {
                            int j6 = k5 - anInt1237;
                            anInt1237 = k5;
                            int k6 = l4 - anInt1238;
                            anInt1238 = l4;
                            if (anInt1022 < 8 && j6 >= -32 && j6 <= 31 && k6 >= -32 && k6 <= 31) {
                                j6 += 32;
                                k6 += 32;
                                outputStream.writeShort((anInt1022 << 12) + (j6 << 6) + k6);
                                anInt1022 = 0;
                            } else if (anInt1022 < 8) {
                                outputStream.write24BitInt(0x800000 + (anInt1022 << 19) + i6);
                                anInt1022 = 0;
                            } else {
                                outputStream.writeInt(0xc0000000 + (anInt1022 << 19) + i6);
                                anInt1022 = 0;
                            }
                        }
                    }
                    outputStream.writeSizeByte(outputStream.offset - j2);
                    if (j3 >= mouseRecorder.cacheIndex) {
                        mouseRecorder.cacheIndex = 0;
                    } else {
                        mouseRecorder.cacheIndex -= j3;
                        for (int i5 = 0; i5 < mouseRecorder.cacheIndex; i5++) {
                            mouseRecorder.mouseXCache[i5] = mouseRecorder.mouseXCache[i5 + j3];
                            mouseRecorder.mouseYCache[i5] = mouseRecorder.mouseYCache[i5 + j3];
                        }
                    }
                }
            } else {
                mouseRecorder.cacheIndex = 0;
            }
        }
        if (super.clickMode3 != 0) {
            long l = (super.aLong29 - aLong1220) / 50L;
            if (l > 4095L) {
                l = 4095L;
            }
            aLong1220 = super.aLong29;
            int k2 = super.saveClickY;
            if (k2 < 0) {
                k2 = 0;
            } else if (k2 > 502) {
                k2 = 502;
            }
            int k3 = super.saveClickX;
            if (k3 < 0) {
                k3 = 0;
            } else if (k3 > 764) {
                k3 = 764;
            }
            int k4 = k2 * 765 + k3;
            int j5 = 0;
            if (super.clickMode3 == 2) {
                j5 = 1;
            }
            int l5 = (int) l;
            outputStream.writeOpcode(241);
            outputStream.writeInt((l5 << 20) + (j5 << 19) + k4);
        }
        if (anInt1016 > 0) {
            anInt1016--;
        }
        if (super.heldKeys[1] == 1 || super.heldKeys[2] == 1 || super.heldKeys[3] == 1 || super.heldKeys[4] == 1) {
            aBoolean1017 = true;
        }
        if (aBoolean1017 && anInt1016 <= 0) {
            anInt1016 = 20;
            aBoolean1017 = false;
            outputStream.writeOpcode(86);
            outputStream.writeShort(cameraY);
            outputStream.writeShortA(cameraX);
        }
        if (super.awtFocus && !aBoolean954) {
            aBoolean954 = true;
            outputStream.writeOpcode(3);
            outputStream.writeByte(1);
        }
        if (!super.awtFocus && aBoolean954) {
            aBoolean954 = false;
            outputStream.writeOpcode(3);
            outputStream.writeByte(0);
        }
        processLoadState();
        method115();
        method90();
        anInt1009++;
        if (anInt1009 > 750) {
            dropClient();
        }
        method114();
        method95();
        method38();
        anInt945++;
        if (crossState != 0) {
            crossIndex += 20;
            if (crossIndex >= 400) {
                crossState = 0;
            }
        }
        if (atInventoryInterfaceType != 0) {
            atInventoryLoopCycle++;
            if (atInventoryLoopCycle >= 15) {
                if (atInventoryInterfaceType == 2) {
                    needDrawTabArea = true;
                }
                if (atInventoryInterfaceType == 3) {
                    inputTaken = true;
                }
                atInventoryInterfaceType = 0;
            }
        }
        if (activeInterfaceType != 0) {
            anInt989++;
            if (super.mouseX > anInt1087 + 5 || super.mouseX < anInt1087 - 5 || super.mouseY > anInt1088 + 5 || super.mouseY < anInt1088 - 5) {
                aBoolean1242 = true;
            }
            if (super.clickMode2 == 0) {
                if (activeInterfaceType == 2) {
                    needDrawTabArea = true;
                }
                if (activeInterfaceType == 3) {
                    inputTaken = true;
                }
                activeInterfaceType = 0;
                if (aBoolean1242 && anInt989 >= 5) {
                    lastActiveInvInterface = -1;
                    processRightClick();
                    if (lastActiveInvInterface == anInt1084 && mouseInvInterfaceIndex != anInt1085) {
                        Interface component = Interface.cachedInterfaces[anInt1084];
                        int j1 = 0;
                        if (anInt913 == 1 && component.contentType == 206) {
                            j1 = 1;
                        }
                        if (component.inv[mouseInvInterfaceIndex] <= 0) {
                            j1 = 0;
                        }
                        if (component.aBoolean235) {
                            int l2 = anInt1085;
                            int l3 = mouseInvInterfaceIndex;
                            component.inv[l3] = component.inv[l2];
                            component.invStackSizes[l3] = component.invStackSizes[l2];
                            component.inv[l2] = -1;
                            component.invStackSizes[l2] = 0;
                        } else if (j1 == 1) {
                            int i3 = anInt1085;
                            for (int i4 = mouseInvInterfaceIndex; i3 != i4;) {
                                if (i3 > i4) {
                                    component.swapInventoryItems(i3, i3 - 1);
                                    i3--;
                                } else if (i3 < i4) {
                                    component.swapInventoryItems(i3, i3 + 1);
                                    i3++;
                                }
                            }
                        } else {
                            component.swapInventoryItems(anInt1085, mouseInvInterfaceIndex);
                        }
                        outputStream.writeOpcode(214);
                        outputStream.writeLEShortA(anInt1084);
                        outputStream.writeByteC(j1);
                        outputStream.writeLEShortA(anInt1085);
                        outputStream.writeLEShort(mouseInvInterfaceIndex);
                    }
                } else if ((anInt1253 == 1 || menuHasAddFriend(menuActionCount - 1)) && menuActionCount > 2) {
                    determineMenuSize();
                } else if (menuActionCount > 0) {
                    doAction(menuActionCount - 1);
                }
                atInventoryLoopCycle = 10;
                super.clickMode3 = 0;
            }
        }
        if (SceneGraph.anInt470 != -1) {
            int k = SceneGraph.anInt470;
            int k1 = SceneGraph.anInt471;
            boolean flag = doWalkTo(0, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, k1, Client.myPlayer.walkQueueX[0], true, k);
            SceneGraph.anInt470 = -1;
            if (flag) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 1;
                crossIndex = 0;
            }
        }
        if (super.clickMode3 == 1 && aString844 != null) {
            aString844 = null;
            inputTaken = true;
            super.clickMode3 = 0;
        }
        processMenuClick();
        processMainScreenClick();
        processTabClick();
        processChatModeClick();
        if (super.clickMode2 == 1 || super.clickMode3 == 1) {
            anInt1213++;
        }
        if (loadingStage == 2) {
            processCameraRotation();
        }
        if (loadingStage == 2 && aBoolean1160) {
            calcCameraPos();
        }
        for (int i1 = 0; i1 < 5; i1++) {
            anIntArray1030[i1]++;
        }
        processInput();
        super.idleTime++;
        if (super.idleTime > 4500) {
            anInt1011 = 250;
            super.idleTime -= 500;
            outputStream.writeOpcode(202);
        }
        anInt988++;
        if (anInt988 > 500) {
            anInt988 = 0;
            int l1 = (int) (Math.random() * 8D);
            if ((l1 & 1) == 1) {
                anInt1278 += anInt1279;
            }
            if ((l1 & 2) == 2) {
                anInt1131 += anInt1132;
            }
            if ((l1 & 4) == 4) {
                anInt896 += anInt897;
            }
        }
        if (anInt1278 < -50) {
            anInt1279 = 2;
        }
        if (anInt1278 > 50) {
            anInt1279 = -2;
        }
        if (anInt1131 < -55) {
            anInt1132 = 2;
        }
        if (anInt1131 > 55) {
            anInt1132 = -2;
        }
        if (anInt896 < -40) {
            anInt897 = 1;
        }
        if (anInt896 > 40) {
            anInt897 = -1;
        }
        anInt1254++;
        if (anInt1254 > 500) {
            anInt1254 = 0;
            int i2 = (int) (Math.random() * 8D);
            if ((i2 & 1) == 1) {
                minimapInt2 += anInt1210;
            }
            if ((i2 & 2) == 2) {
                minimapInt3 += anInt1171;
            }
        }
        if (minimapInt2 < -60) {
            anInt1210 = 2;
        }
        if (minimapInt2 > 60) {
            anInt1210 = -2;
        }
        if (minimapInt3 < -20) {
            anInt1171 = 1;
        }
        if (minimapInt3 > 10) {
            anInt1171 = -1;
        }
        anInt1010++;
        if (anInt1010 > 50) {
            outputStream.writeOpcode(0);
        }
        try {
            if (socketStream != null && outputStream.offset > 0) {
                socketStream.queueBytes(outputStream.offset, outputStream.payload);
                outputStream.offset = 0;
                anInt1010 = 0;
            }
        } catch (IOException exception) {
            dropClient();
        } catch (Exception exception) {
            processLogout();
        }
    }

    private void method63() {
        SceneObject object = (SceneObject) aClass19_1179.getFront();
        for (; object != null; object = (SceneObject) aClass19_1179.getNext()) {
            if (object.anInt1294 == -1) {
                object.anInt1302 = 0;
                method89(object);
            } else {
                object.unlink();
            }
        }
    }

    private void resetImageProducers() {
        if (topLeftCanvas != null) {
            return;
        }
        super.graphicsBuffer = null;
        aGraphicsBuffer_1166 = null;
        minimapCanvas = null;
        aGraphicsBuffer_1163 = null;
        gameScreenCanvas = null;
        aGraphicsBuffer_1123 = null;
        aGraphicsBuffer_1124 = null;
        aGraphicsBuffer_1125 = null;
        leftFlameCanvas = new GraphicsBuffer(128, 265, getGameComponent());
        Graphics2D.resetPixels();
        rightFlameCanvas = new GraphicsBuffer(128, 265, getGameComponent());
        Graphics2D.resetPixels();
        topLeftCanvas = new GraphicsBuffer(509, 171, getGameComponent());
        Graphics2D.resetPixels();
        bottomLeftCanvas = new GraphicsBuffer(360, 132, getGameComponent());
        Graphics2D.resetPixels();
        leftCanvas = new GraphicsBuffer(360, 200, getGameComponent());
        Graphics2D.resetPixels();
        titleBoxLeftCanvas = new GraphicsBuffer(202, 238, getGameComponent());
        Graphics2D.resetPixels();
        bottomRightCanvas = new GraphicsBuffer(203, 238, getGameComponent());
        Graphics2D.resetPixels();
        smallLeftCanvas = new GraphicsBuffer(74, 94, getGameComponent());
        Graphics2D.resetPixels();
        smallRightCanvas = new GraphicsBuffer(75, 94, getGameComponent());
        Graphics2D.resetPixels();
        if (titleArchive != null) {
            drawTitleScreen();
            loadTitleScreen();
        }
        welcomeScreenRaised = true;
    }

    void drawLoadingText(int percentLoaded, String text) {
        anInt1079 = percentLoaded;
        aString1049 = text;
        resetImageProducers();
        if (titleArchive == null) {
            super.drawLoadingText(percentLoaded, text);
            return;
        }
        leftCanvas.initDrawingArea();
        char c = '\u0168';
        char c1 = '\310';
        byte byte1 = 20;
        boldFont.drawANCText(0xffffff, "RuneScape is loading - please wait...", c1 / 2 - 26 - byte1, c / 2);
        int j = c1 / 2 - 18 - byte1;
        Graphics2D.drawRect(c / 2 - 152, 304, 34, 0x8c1111, j);
        Graphics2D.drawRect(c / 2 - 151, 302, 32, 0, j + 1);
        Graphics2D.fillRect(30, j + 2, c / 2 - 150, 0x8c1111, percentLoaded * 3);
        Graphics2D.fillRect(30, j + 2, c / 2 - 150 + percentLoaded * 3, 0, 300 - percentLoaded * 3);
        boldFont.drawANCText(0xffffff, text, c1 / 2 + 5 - byte1, c / 2);
        leftCanvas.drawGraphics(171, super.graphics, 202);
        if (welcomeScreenRaised) {
            welcomeScreenRaised = false;
            if (!aBoolean831) {
                leftFlameCanvas.drawGraphics(0, super.graphics, 0);
                rightFlameCanvas.drawGraphics(0, super.graphics, 637);
            }
            topLeftCanvas.drawGraphics(0, super.graphics, 128);
            bottomLeftCanvas.drawGraphics(371, super.graphics, 202);
            titleBoxLeftCanvas.drawGraphics(265, super.graphics, 0);
            bottomRightCanvas.drawGraphics(265, super.graphics, 562);
            smallLeftCanvas.drawGraphics(171, super.graphics, 128);
            smallRightCanvas.drawGraphics(171, super.graphics, 562);
        }
    }

    private void method65(int arg0, int arg1, int arg2, int arg4, Interface component, int arg6, boolean flag, int arg8) {
        int anInt992;
        if (aBoolean972) {
            anInt992 = 32;
        } else {
            anInt992 = 0;
        }
        aBoolean972 = false;
        if (arg2 >= arg0 && arg2 < arg0 + 16 && arg4 >= arg6 && arg4 < arg6 + 16) {
            component.scrollPosition -= anInt1213 * 4;
            if (flag) {
                needDrawTabArea = true;
            }
        } else if (arg2 >= arg0 && arg2 < arg0 + 16 && arg4 >= arg6 + arg1 - 16 && arg4 < arg6 + arg1) {
            component.scrollPosition += anInt1213 * 4;
            if (flag) {
                needDrawTabArea = true;
            }
        } else if (arg2 >= arg0 - anInt992 && arg2 < arg0 + 16 + anInt992 && arg4 >= arg6 + 16 && arg4 < arg6 + arg1 - 16 && anInt1213 > 0) {
            int l1 = (arg1 - 32) * arg1 / arg8;
            if (l1 < 8) {
                l1 = 8;
            }
            int i2 = arg4 - arg6 - 16 - l1 / 2;
            int j2 = arg1 - 32 - l1;
            component.scrollPosition = (arg8 - arg1) * i2 / j2;
            if (flag) {
                needDrawTabArea = true;
            }
            aBoolean972 = true;
        }
    }

    private boolean method66(int arg0, int arg1, int arg2) {
        int i1 = arg0 >> 14 & 0x7fff;
        int j1 = sceneGraph.method304(plane, arg2, arg1, arg0);
        if (j1 == -1) {
            return false;
        }
        int k1 = j1 & 0x1f;
        int l1 = j1 >> 6 & 3;
        if (k1 == 10 || k1 == 11 || k1 == 22) {
            ObjectDefinition objectDefinition = ObjectDefinition.forId(i1);
            int i2;
            int j2;
            if (l1 == 0 || l1 == 2) {
                i2 = objectDefinition.ySize;
                j2 = objectDefinition.xSize;
            } else {
                i2 = objectDefinition.xSize;
                j2 = objectDefinition.ySize;
            }
            int k2 = objectDefinition.anInt768;
            if (l1 != 0) {
                k2 = (k2 << l1 & 0xf) + (k2 >> 4 - l1);
            }
            doWalkTo(2, 0, j2, 0, Client.myPlayer.walkQueueY[0], i2, k2, arg1, Client.myPlayer.walkQueueX[0], false, arg2);
        } else {
            doWalkTo(2, l1, 0, k1 + 1, Client.myPlayer.walkQueueY[0], 0, 0, arg1, Client.myPlayer.walkQueueX[0], false, arg2);
        }
        crossX = super.saveClickX;
        crossY = super.saveClickY;
        crossState = 2;
        crossIndex = 0;
        return true;
    }

    private Archive archiveForName(int cacheIndex, String dataType, String archiveName, int archiveIndex, int loadedPercent) {
        byte buf[] = null;
        int off = 5;
        try {
            if (cacheIndices[0] != null) {
                buf = cacheIndices[0].get(cacheIndex);
            }
        } catch (Exception exception) {
        }
        if (buf != null) {
            // aCRC32_930.reset();
            // aCRC32_930.update(abyte0);
            // int i1 = (int)aCRC32_930.getValue();
            // if(i1 != j)
        }
        if (buf != null) {
            // StreamLoader streamLoader = new StreamLoader(abyte0);
            // return streamLoader;
        }
        int j1 = 0;
        while (buf == null) {
            String s2 = "Unknown error";
            drawLoadingText(loadedPercent, "Requesting " + dataType);
            try {
                int k1 = 0;
                DataInputStream datainputstream = requestFile(archiveName + archiveIndex);
                byte src[] = new byte[6];
                datainputstream.readFully(src, 0, 6);
                Stream stream = new Stream(src);
                stream.offset = 3;
                int i2 = stream.get24BitInt() + 6;
                int j2 = 6;
                buf = new byte[i2];
                System.arraycopy(src, 0, buf, 0, 6);
                while (j2 < i2) {
                    int l2 = i2 - j2;
                    if (l2 > 1000) {
                        l2 = 1000;
                    }
                    int j3 = datainputstream.read(buf, j2, l2);
                    if (j3 < 0) {
                        s2 = "Length error: " + j2 + "/" + i2;
                        throw new IOException("EOF");
                    }
                    j2 += j3;
                    int k3 = j2 * 100 / i2;
                    if (k3 != k1) {
                        drawLoadingText(loadedPercent, "Loading " + dataType + " - " + k3 + "%");
                    }
                    k1 = k3;
                }
                datainputstream.close();
                try {
                    if (cacheIndices[0] != null) {
                        cacheIndices[0].put(buf.length, buf, cacheIndex);
                    }
                } catch (Exception exception) {
                    cacheIndices[0] = null;
                }
                if (buf != null) {
                    // aCRC32_930.reset();
                    // aCRC32_930.update(abyte0);
                    // int i3 = (int)aCRC32_930.getValue();
                    // if(i3 != j)
                    // {
                    // abyte0 = null;
                    // j1++;
                    // s2 = "Checksum error: " + i3;
                    // }
                }
            } catch (IOException ioexception) {
                if (s2.equals("Unknown error")) {
                    s2 = "Connection error";
                }
                buf = null;
            } catch (NullPointerException exception) {
                s2 = "Null error";
                buf = null;
                if (!Signlink.reporterror) {
                    return null;
                }
            } catch (ArrayIndexOutOfBoundsException exception) {
                s2 = "Bounds error";
                buf = null;
                if (!Signlink.reporterror) {
                    return null;
                }
            } catch (Exception exception) {
                s2 = "Unexpected error";
                buf = null;
                if (!Signlink.reporterror) {
                    return null;
                }
            }
            if (buf == null) {
                for (int l1 = off; l1 > 0; l1--) {
                    if (j1 >= 3) {
                        drawLoadingText(loadedPercent, "Game updated - please reload page");
                        l1 = 10;
                    } else {
                        drawLoadingText(loadedPercent, s2 + " - Retrying in " + l1);
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception exception) {
                    }
                }
                off *= 2;
                if (off > 60) {
                    off = 60;
                }
                aBoolean872 = !aBoolean872;
            }
        }
        Archive archive_1 = new Archive(buf);
        return archive_1;
    }

    private void dropClient() {
        if (anInt1011 > 0) {
            processLogout();
            return;
        }
        gameScreenCanvas.initDrawingArea();
        aFont_1271.drawANCText(0, "Connection lost", 144, 257);
        aFont_1271.drawANCText(0xffffff, "Connection lost", 143, 256);
        aFont_1271.drawANCText(0, "Please wait - attempting to reestablish", 159, 257);
        aFont_1271.drawANCText(0xffffff, "Please wait - attempting to reestablish", 158, 256);
        gameScreenCanvas.drawGraphics(4, super.graphics, 4);
        anInt1021 = 0;
        destX = 0;
        Socket rsSocket = socketStream;
        loggedIn = false;
        loginFailures = 0;
        login(myUsername, myPassword, true);
        if (!loggedIn) {
            processLogout();
        }
        try {
            rsSocket.close();
        } catch (Exception exception) {
        }
    }

    private void doAction(int id) {
        if (id < 0) {
            return;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            inputTaken = true;
        }
        int command2 = menuActionCmd2[id];
        int command3 = menuActionCmd3[id];
        int actionId = menuActionOpcode[id];
        int command1 = menuActionCmd1[id];
        if (actionId >= 2000) {
            actionId -= 2000;
        }
        if (actionId == 20) {
            Npc npc = localNpcs[command1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, npc.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, npc.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(155);
                outputStream.writeLEShort(command1);
            }
        }
        if (actionId == 6 || actionId == 484) { // Player following
            String s1 = menuActionName[id];
            int l1 = s1.indexOf("@whi@");
            if (l1 != -1) {
                s1 = s1.substring(l1 + 5).trim();
                String formattedName = TextUtil.formatName(TextUtil.longToName(TextUtil.nameToLong(s1)));
                boolean flag9 = false;
                for (int j3 = 0; j3 < localPlayerCount; j3++) {
                    Player player = localPlayers[playerIndices[j3]];
                    if (player == null || player.name == null || !player.name.equalsIgnoreCase(formattedName)) {
                        continue;
                    }
                    doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, player.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, player.walkQueueX[0]);
                    if (actionId == 6) {
                        Client.anInt1188 += command1;
                        if (Client.anInt1188 >= 90) {
                            outputStream.writeOpcode(136);
                            Client.anInt1188 = 0;
                        }
                        outputStream.writeOpcode(128);
                        outputStream.writeShort(playerIndices[j3]);
                    }
                    if (actionId == 484) {
                        outputStream.writeOpcode(139);
                        outputStream.writeLEShort(playerIndices[j3]);
                    }
                    flag9 = true;
                    break;
                }
                if (!flag9) {
                    pushMessage("Unable to find " + formattedName, 0, "");
                }
            }
        }
        if (actionId == 27) {
            Player player = localPlayers[command1];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, player.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, player.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                Client.anInt986 += command1;
                if (Client.anInt986 >= 54) {
                    outputStream.writeOpcode(189);
                    outputStream.writeByte(234);
                    Client.anInt986 = 0;
                }
                outputStream.writeOpcode(73);
                outputStream.writeLEShort(command1);
            }
        }
        if (actionId == 42 || actionId == 322 || actionId == 337 || actionId == 792) {
            String s = menuActionName[id];
            int playerInput = s.indexOf("@whi@");
            if (playerInput != -1) {
                long formattedName = TextUtil.nameToLong(s.substring(playerInput + 5).trim());
                if (actionId == 42) {
                    addIgnore(formattedName);
                }
                if (actionId == 322) {
                    delIgnore(formattedName);
                }
                if (actionId == 337) {
                    addFriend(formattedName);
                }
                if (actionId == 792) {
                    delFriend(formattedName);
                }
            }
        }
        if (actionId == 53) {
            outputStream.writeOpcode(135);
            outputStream.writeLEShort(command2);
            outputStream.writeShortA(command3);
            outputStream.writeLEShort(command1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 62 && method66(command1, command3, command2)) {
            outputStream.writeOpcode(192);
            outputStream.writeShort(anInt1284);
            outputStream.writeLEShort(command1 >> 14 & 0x7fff);
            outputStream.writeLEShortA(command3 + baseY);
            outputStream.writeLEShort(anInt1283);
            outputStream.writeLEShortA(command2 + baseX);
            outputStream.writeShort(anInt1285);
        }
        if (actionId == 74) {
            outputStream.writeOpcode(122);
            outputStream.writeLEShortA(command3);
            outputStream.writeShortA(command2);
            outputStream.writeLEShort(command1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 78) {
            outputStream.writeOpcode(117);
            outputStream.writeLEShortA(command3);
            outputStream.writeLEShortA(command1);
            outputStream.writeLEShort(command2);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 94) {
            boolean flag5 = doWalkTo(2, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            if (!flag5) {
                flag5 = doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossState = 2;
            crossIndex = 0;
            outputStream.writeOpcode(181);
            outputStream.writeLEShort(command3 + baseY);
            outputStream.writeShort(command1);
            outputStream.writeLEShort(command2 + baseX);
            outputStream.writeShortA(anInt1137);
        }
        if (actionId == 113) {
            method66(command1, command3, command2);
            outputStream.writeOpcode(70);
            outputStream.writeLEShort(command2 + baseX);
            outputStream.writeShort(command3 + baseY);
            outputStream.writeLEShortA(command1 >> 14 & 0x7fff);
        }
        if (actionId == 169) {
            outputStream.writeOpcode(185);
            outputStream.writeShort(command3);
            Interface component = Interface.cachedInterfaces[command3];
            if (component.valueIndices != null && component.valueIndices[0][0] == 5) {
                int l2 = component.valueIndices[0][1];
                configStates[l2] = 1 - configStates[l2];
                method33(l2);
                needDrawTabArea = true;
            }
        }
        if (actionId == 200) {
            closeOpenInterfaces();
        }
        if (actionId == 213) {
            boolean flag3 = doWalkTo(2, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            if (!flag3) {
                flag3 = doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossState = 2;
            crossIndex = 0;
            outputStream.writeOpcode(79);
            outputStream.writeLEShort(command3 + baseY);
            outputStream.writeShort(command1);
            outputStream.writeShortA(command2 + baseX);
        }
        if (actionId == 225) {
            Npc npc = localNpcs[command1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, npc.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, npc.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                Client.anInt1226 += command1;
                if (Client.anInt1226 >= 85) {
                    outputStream.writeOpcode(230);
                    outputStream.writeByte(239);
                    Client.anInt1226 = 0;
                }
                outputStream.writeOpcode(17);
                outputStream.writeLEShortA(command1);
            }
        }
        if (actionId == 234) {
            boolean flag1 = doWalkTo(2, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            if (!flag1) {
                flag1 = doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossState = 2;
            crossIndex = 0;
            outputStream.writeOpcode(236);
            outputStream.writeLEShort(command3 + baseY);
            outputStream.writeShort(command1);
            outputStream.writeLEShort(command2 + baseX);
        }
        if (actionId == 244) {
            boolean flag7 = doWalkTo(2, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            if (!flag7) {
                flag7 = doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossState = 2;
            crossIndex = 0;
            outputStream.writeOpcode(253);
            outputStream.writeLEShort(command2 + baseX);
            outputStream.writeLEShortA(command3 + baseY);
            outputStream.writeShortA(command1);
        }
        if (actionId == 315) {
            Interface component = Interface.cachedInterfaces[command3];
            boolean flag8 = true;
            if (component.contentType > 0) {
                flag8 = promptUserForInput(component);
            }
            if (flag8) {
                outputStream.writeOpcode(185);
                outputStream.writeShort(command3);
            }
        }
        if (actionId == 365) {
            Player player = localPlayers[command1];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, player.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, player.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(249);
                outputStream.writeShortA(command1);
                outputStream.writeLEShort(anInt1137);
            }
        }
        if (actionId == 412) {
            Npc npc = localNpcs[command1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, npc.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, npc.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(72);
                outputStream.writeShortA(command1);
            }
        }
        if (actionId == 413) {
            Npc npc = localNpcs[command1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, npc.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, npc.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(131);
                outputStream.writeLEShortA(command1);
                outputStream.writeShortA(anInt1137);
            }
        }
        if (actionId == 431) {
            outputStream.writeOpcode(129);
            outputStream.writeShortA(command2);
            outputStream.writeShort(command3);
            outputStream.writeShortA(command1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 447) {
            itemSelected = 1;
            anInt1283 = command2;
            anInt1284 = command3;
            anInt1285 = command1;
            selectedItemName = ItemDefinition.forId(command1).name;
            spellSelected = 0;
            needDrawTabArea = true;
            return;
        }
        if (actionId == 454) {
            outputStream.writeOpcode(41);
            outputStream.writeShort(command1);
            outputStream.writeShortA(command2);
            outputStream.writeShortA(command3);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 478) {
            Npc npc = localNpcs[command1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, npc.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, npc.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                if ((command1 & 3) == 0) {
                    Client.anInt1155++;
                }
                if (Client.anInt1155 >= 53) {
                    outputStream.writeOpcode(85);
                    outputStream.writeByte(66);
                    Client.anInt1155 = 0;
                }
                outputStream.writeOpcode(18);
                outputStream.writeLEShort(command1);
            }
        }
        if (actionId == 491) {
            Player player = localPlayers[command1];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, player.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, player.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(14);
                outputStream.writeShortA(anInt1284);
                outputStream.writeShort(command1);
                outputStream.writeShort(anInt1285);
                outputStream.writeLEShort(anInt1283);
            }
        }
        if (actionId == 493) {
            outputStream.writeOpcode(75);
            outputStream.writeLEShortA(command3);
            outputStream.writeLEShort(command2);
            outputStream.writeShortA(command1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 502) {
            method66(command1, command3, command2);
            outputStream.writeOpcode(132);
            outputStream.writeLEShortA(command2 + baseX);
            outputStream.writeShort(command1 >> 14 & 0x7fff);
            outputStream.writeShortA(command3 + baseY);
        }
        if (actionId == 511) {
            boolean flag2 = doWalkTo(2, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            if (!flag2) {
                flag2 = doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossState = 2;
            crossIndex = 0;
            outputStream.writeOpcode(25);
            outputStream.writeLEShort(anInt1284);
            outputStream.writeShortA(anInt1285);
            outputStream.writeShort(command1);
            outputStream.writeShortA(command3 + baseY);
            outputStream.writeLEShortA(anInt1283);
            outputStream.writeShort(command2 + baseX);
        }
        if (actionId == 516) {
            if (!menuOpen) {
                sceneGraph.method312(super.saveClickY - 4, super.saveClickX - 4);
            } else {
                sceneGraph.method312(command3 - 4, command2 - 4);
            }
        }
        if (actionId == 539) {
            outputStream.writeOpcode(16);
            outputStream.writeShortA(command1);
            outputStream.writeLEShortA(command2);
            outputStream.writeLEShortA(command3);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 543) {
            outputStream.writeOpcode(237);
            outputStream.writeShort(command2);
            outputStream.writeShortA(command1);
            outputStream.writeShort(command3);
            outputStream.writeShortA(anInt1137);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 561) {
            Player player = localPlayers[command1];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, player.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, player.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                Client.anInt1188 += command1;
                if (Client.anInt1188 >= 90) {
                    outputStream.writeOpcode(136);
                    Client.anInt1188 = 0;
                }
                outputStream.writeOpcode(128);
                outputStream.writeShort(command1);
            }
        }
        if (actionId == 567) {
            boolean flag6 = doWalkTo(2, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            if (!flag6) {
                flag6 = doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossState = 2;
            crossIndex = 0;
            outputStream.writeOpcode(23);
            outputStream.writeLEShort(command3 + baseY);
            outputStream.writeLEShort(command1);
            outputStream.writeLEShort(command2 + baseX);
        }
        if (actionId == 577) {
            Player player = localPlayers[command1];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, player.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, player.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(139);
                outputStream.writeLEShort(command1);
            }
        }
        if (actionId == 582) {
            Npc npc = localNpcs[command1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, npc.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, npc.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(57);
                outputStream.writeShortA(anInt1285);
                outputStream.writeShortA(command1);
                outputStream.writeLEShort(anInt1283);
                outputStream.writeShortA(anInt1284);
            }
        }
        if (actionId == 606) {
            String s2 = menuActionName[id];
            int j2 = s2.indexOf("@whi@");
            if (j2 != -1) {
                if (openInterfaceId == -1) {
                    closeOpenInterfaces();
                    reportAbuseInput = s2.substring(j2 + 5).trim();
                    canMute = false;
                    for (Interface element : Interface.cachedInterfaces) {
                        if (element == null || element.contentType != 600) {
                            continue;
                        }
                        reportAbuseInterfaceID = openInterfaceId = element.parentId;
                        break;
                    }
                } else {
                    pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
                }
            }
        }
        if (actionId == 626) {
            Interface component = Interface.cachedInterfaces[command3];
            spellSelected = 1;
            anInt1137 = command3;
            spellUsableOn = component.spellUsableOn;
            itemSelected = 0;
            needDrawTabArea = true;
            String s4 = component.selectedActionName;
            if (s4.indexOf(" ") != -1) {
                s4 = s4.substring(0, s4.indexOf(" "));
            }
            String s8 = component.selectedActionName;
            if (s8.indexOf(" ") != -1) {
                s8 = s8.substring(s8.indexOf(" ") + 1);
            }
            spellTooltip = s4 + " " + component.spellName + " " + s8;
            if (spellUsableOn == 16) {
                needDrawTabArea = true;
                tabID = 3;
                tabAreaAltered = true;
            }
            return;
        }
        if (actionId == 632) {
            outputStream.writeOpcode(145);
            outputStream.writeShortA(command3);
            outputStream.writeShortA(command2);
            outputStream.writeShortA(command1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 639) {
            String s3 = menuActionName[id];
            int k2 = s3.indexOf("@whi@");
            if (k2 != -1) {
                long formattedName = TextUtil.nameToLong(s3.substring(k2 + 5).trim());
                int k3 = -1;
                for (int i4 = 0; i4 < friendsCount; i4++) {
                    if (friendsNamesAsLongs[i4] != formattedName) {
                        continue;
                    }
                    k3 = i4;
                    break;
                }
                if (k3 != -1 && friendsWorlds[k3] > 0) {
                    inputTaken = true;
                    inputDialogState = 0;
                    messagePromptRaised = true;
                    promptInput = "";
                    friendsListAction = 3;
                    fromPlayer = friendsNamesAsLongs[k3];
                    aString1121 = "Enter message to send to " + friendsNames[k3];
                }
            }
        }
        if (actionId == 646) {
            outputStream.writeOpcode(185);
            outputStream.writeShort(command3);
            Interface component = Interface.cachedInterfaces[command3];
            if (component.valueIndices != null && component.valueIndices[0][0] == 5) {
                int i2 = component.valueIndices[0][1];
                if (configStates[i2] != component.requiredValues[0]) {
                    configStates[i2] = component.requiredValues[0];
                    method33(i2);
                    needDrawTabArea = true;
                }
            }
        }
        if (actionId == 652) {
            boolean flag4 = doWalkTo(2, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            if (!flag4) {
                flag4 = doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, command3, Client.myPlayer.walkQueueX[0], false, command2);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossState = 2;
            crossIndex = 0;
            outputStream.writeOpcode(156);
            outputStream.writeShortA(command2 + baseX);
            outputStream.writeLEShort(command3 + baseY);
            outputStream.writeLEShortA(command1);
        }
        if (actionId == 679 && !aBoolean1149) {
            outputStream.writeOpcode(40);
            outputStream.writeShort(command3);
            aBoolean1149 = true;
        }
        if (actionId == 729) {
            Player player = localPlayers[command1];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, player.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, player.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(39);
                outputStream.writeLEShort(command1);
            }
        }
        if (actionId == 779) {
            Player class30_sub2_sub4_sub1_sub2_1 = localPlayers[command1];
            if (class30_sub2_sub4_sub1_sub2_1 != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, class30_sub2_sub4_sub1_sub2_1.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, class30_sub2_sub4_sub1_sub2_1.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                outputStream.writeOpcode(153);
                outputStream.writeLEShort(command1);
            }
        }
        if (actionId == 847) {
            outputStream.writeOpcode(87);
            outputStream.writeShortA(command1);
            outputStream.writeShort(command3);
            outputStream.writeShortA(command2);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 867) {
            if ((command1 & 3) == 0) {
                Client.anInt1175++;
            }
            if (Client.anInt1175 >= 59) {
                outputStream.writeOpcode(200);
                outputStream.writeShort(25501);
                Client.anInt1175 = 0;
            }
            outputStream.writeOpcode(43);
            outputStream.writeLEShort(command3);
            outputStream.writeShortA(command1);
            outputStream.writeShortA(command2);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 870) {
            outputStream.writeOpcode(53);
            outputStream.writeShort(command2);
            outputStream.writeShortA(anInt1283);
            outputStream.writeLEShortA(command1);
            outputStream.writeShort(anInt1284);
            outputStream.writeLEShort(anInt1285);
            outputStream.writeShort(command3);
            atInventoryLoopCycle = 0;
            atInventoryInterface = command3;
            atInventoryIndex = command2;
            atInventoryInterfaceType = 2;
            if (Interface.cachedInterfaces[command3].parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Interface.cachedInterfaces[command3].parentId == backDialogID) {
                atInventoryInterfaceType = 3;
            }
        }
        if (actionId == 872) {
            method66(command1, command3, command2);
            outputStream.writeOpcode(234);
            outputStream.writeLEShortA(command2 + baseX);
            outputStream.writeShortA(command1 >> 14 & 0x7fff);
            outputStream.writeLEShortA(command3 + baseY);
        }
        if (actionId == 900) {
            method66(command1, command3, command2);
            outputStream.writeOpcode(252);
            outputStream.writeLEShortA(command1 >> 14 & 0x7fff);
            outputStream.writeLEShort(command3 + baseY);
            outputStream.writeShortA(command2 + baseX);
        }
        if (actionId == 956 && method66(command1, command3, command2)) {
            outputStream.writeOpcode(35);
            outputStream.writeLEShort(command2 + baseX);
            outputStream.writeShortA(anInt1137);
            outputStream.writeShortA(command3 + baseY);
            outputStream.writeLEShort(command1 >> 14 & 0x7fff);
        }
        if (actionId == 965) {
            Npc npc = localNpcs[command1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, Client.myPlayer.walkQueueY[0], 1, 0, npc.walkQueueY[0], Client.myPlayer.walkQueueX[0], false, npc.walkQueueX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossState = 2;
                crossIndex = 0;
                Client.anInt1134++;
                if (Client.anInt1134 >= 96) {
                    outputStream.writeOpcode(152);
                    outputStream.writeByte(88);
                    Client.anInt1134 = 0;
                }
                outputStream.writeOpcode(21);
                outputStream.writeShort(command1);
            }
        }
        if (actionId == 1025) {
            Npc npc = localNpcs[command1];
            String indefinite = "a";
            if (npc != null) {
                NpcDefinition npcDefinition = npc.desc;
                if (npcDefinition.childIds != null) {
                    npcDefinition = npcDefinition.method161();
                }
                if (npcDefinition != null) {
                    String examineInfo;
                    if (npcDefinition.description != null) {
                        examineInfo = new String(npcDefinition.description);
                    } else {
                        if (npcDefinition.name.charAt(0) == 'A' || npcDefinition.name.charAt(0) == 'E' || npcDefinition.name.charAt(0) == 'I' || npcDefinition.name.charAt(0) == 'O' || npcDefinition.name.charAt(0) == 'U') {
                            indefinite = "an";
                        }
                        examineInfo = "It's " + indefinite + " " + npcDefinition.name + ".";
                    }
                    pushMessage(examineInfo, 0, "");
                }
            }
        }
        if (actionId == 1062) {
            Client.anInt924 += baseX;
            if (Client.anInt924 >= 113) {
                outputStream.writeOpcode(183);
                outputStream.write24BitInt(0xe63271);
                Client.anInt924 = 0;
            }
            method66(command1, command3, command2);
            outputStream.writeOpcode(228);
            outputStream.writeShortA(command1 >> 14 & 0x7fff);
            outputStream.writeShortA(command3 + baseY);
            outputStream.writeShort(command2 + baseX);
        }
        if (actionId == 1125) {
            ItemDefinition itemDefinition = ItemDefinition.forId(command1);
            String indefinite = "a";
            Interface component = Interface.cachedInterfaces[command3];
            String s5;
            if (component != null && component.invStackSizes[command2] >= 0x186a0) {
                s5 = component.invStackSizes[command2] + " x " + itemDefinition.name;
            } else if (itemDefinition.description != null) {
                s5 = new String(itemDefinition.description);
            } else {
                if (itemDefinition.name.charAt(0) == 'A' || itemDefinition.name.charAt(0) == 'E' || itemDefinition.name.charAt(0) == 'I' || itemDefinition.name.charAt(0) == 'O' || itemDefinition.name.charAt(0) == 'U') {
                    indefinite = "an";
                }
                s5 = "It's " + indefinite + " " + itemDefinition.name + ".";
            }
            pushMessage(s5, 0, "");
        }
        if (actionId == 1226) {
            int j1 = command1 >> 14 & 0x7fff;
            ObjectDefinition objectDefinition = ObjectDefinition.forId(j1);
            String examineInfo;
            String indefinite = "a";
            if (objectDefinition.description != null) {
                examineInfo = new String(objectDefinition.description);
            } else {
                if (objectDefinition.name.charAt(0) == 'A' || objectDefinition.name.charAt(0) == 'E' || objectDefinition.name.charAt(0) == 'I' || objectDefinition.name.charAt(0) == 'O' || objectDefinition.name.charAt(0) == 'U') {
                    indefinite = "an";
                }
                examineInfo = "It's " + indefinite + " " + objectDefinition.name + ".";
            }
            pushMessage(examineInfo, 0, "");
        }
        if (actionId == 1448) {
            ItemDefinition itemDefinition = ItemDefinition.forId(command1);
            String examineInfo;
            String indefinite = "a";
            if (itemDefinition.description != null) {
                examineInfo = new String(itemDefinition.description);
            } else {
                if (itemDefinition.name.charAt(0) == 'A' || itemDefinition.name.charAt(0) == 'E' || itemDefinition.name.charAt(0) == 'I' || itemDefinition.name.charAt(0) == 'O' || itemDefinition.name.charAt(0) == 'U') {
                    indefinite = "an";
                }
                examineInfo = "It's " + indefinite + " " + itemDefinition.name + ".";
            }
            pushMessage(examineInfo, 0, "");
        }
        itemSelected = 0;
        spellSelected = 0;
        needDrawTabArea = true;
    }

    private void method70() {
        anInt1251 = 0;
        int j = (Client.myPlayer.x >> 7) + baseX;
        int k = (Client.myPlayer.y >> 7) + baseY;
        if (j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136) {
            anInt1251 = 1;
        }
        if (j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535) {
            anInt1251 = 1;
        }
        if (anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062) {
            anInt1251 = 0;
        }
    }

    public void run() {
        if (drawFlames) {
            processFlameDrawing();
        } else {
            super.run();
        }
    }

    private void build3dScreenMenu() {
        if (itemSelected == 0 && spellSelected == 0) {
            menuActionName[menuActionCount] = "Walk here";
            menuActionOpcode[menuActionCount] = 516;
            menuActionCmd2[menuActionCount] = super.mouseX;
            menuActionCmd3[menuActionCount] = super.mouseY;
            menuActionCount++;
        }
        int j = -1;
        for (int k = 0; k < Model.anInt1687; k++) {
            int l = Model.anIntArray1688[k];
            int i1 = l & 0x7f;
            int j1 = l >> 7 & 0x7f;
            int k1 = l >> 29 & 3;
            int l1 = l >> 14 & 0x7fff;
            if (l == j) {
                continue;
            }
            j = l;
            if (k1 == 2 && sceneGraph.method304(plane, i1, j1, l) >= 0) {
                ObjectDefinition objectDefinition = ObjectDefinition.forId(l1);
                if (objectDefinition.childIds != null) {
                    objectDefinition = objectDefinition.method580();
                }
                if (objectDefinition == null) {
                    continue;
                }
                if (itemSelected == 1) {
                    menuActionName[menuActionCount] = "Use " + selectedItemName + " with @cya@" + objectDefinition.name;
                    menuActionOpcode[menuActionCount] = 62;
                    menuActionCmd1[menuActionCount] = l;
                    menuActionCmd2[menuActionCount] = i1;
                    menuActionCmd3[menuActionCount] = j1;
                    menuActionCount++;
                } else if (spellSelected == 1) {
                    if ((spellUsableOn & 4) == 4) {
                        menuActionName[menuActionCount] = spellTooltip + " @cya@" + objectDefinition.name;
                        menuActionOpcode[menuActionCount] = 956;
                        menuActionCmd1[menuActionCount] = l;
                        menuActionCmd2[menuActionCount] = i1;
                        menuActionCmd3[menuActionCount] = j1;
                        menuActionCount++;
                    }
                } else {
                    if (objectDefinition.actions != null) {
                        for (int i2 = 4; i2 >= 0; i2--) {
                            if (objectDefinition.actions[i2] != null) {
                                menuActionName[menuActionCount] = objectDefinition.actions[i2] + " @cya@" + objectDefinition.name;
                                if (i2 == 0) {
                                    menuActionOpcode[menuActionCount] = 502;
                                }
                                if (i2 == 1) {
                                    menuActionOpcode[menuActionCount] = 900;
                                }
                                if (i2 == 2) {
                                    menuActionOpcode[menuActionCount] = 113;
                                }
                                if (i2 == 3) {
                                    menuActionOpcode[menuActionCount] = 872;
                                }
                                if (i2 == 4) {
                                    menuActionOpcode[menuActionCount] = 1062;
                                }
                                menuActionCmd1[menuActionCount] = l;
                                menuActionCmd2[menuActionCount] = i1;
                                menuActionCmd3[menuActionCount] = j1;
                                menuActionCount++;
                            }
                        }
                    }
                    menuActionName[menuActionCount] = "Examine @cya@" + objectDefinition.name;
                    menuActionOpcode[menuActionCount] = 1226;
                    menuActionCmd1[menuActionCount] = objectDefinition.type << 14;
                    menuActionCmd2[menuActionCount] = i1;
                    menuActionCmd3[menuActionCount] = j1;
                    menuActionCount++;
                }
            }
            if (k1 == 1) {
                Npc npc = localNpcs[l1];
                if (npc.desc.size == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
                    for (int j2 = 0; j2 < localNpcCount; j2++) {
                        Npc npc2 = localNpcs[localNpcIndices[j2]];
                        if (npc2 != null && npc2 != npc && npc2.desc.size == 1 && npc2.x == npc.x && npc2.y == npc.y) {
                            buildAtNpcMenu(npc2.desc, localNpcIndices[j2], j1, i1);
                        }
                    }
                    for (int l2 = 0; l2 < localPlayerCount; l2++) {
                        Player player = localPlayers[playerIndices[l2]];
                        if (player != null && player.x == npc.x && player.y == npc.y) {
                            buildAtPlayerMenu(i1, playerIndices[l2], player, j1);
                        }
                    }
                }
                buildAtNpcMenu(npc.desc, l1, j1, i1);
            }
            if (k1 == 0) {
                Player player = localPlayers[l1];
                if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
                    for (int k2 = 0; k2 < localNpcCount; k2++) {
                        Npc class30_sub2_sub4_sub1_sub1_2 = localNpcs[localNpcIndices[k2]];
                        if (class30_sub2_sub4_sub1_sub1_2 != null && class30_sub2_sub4_sub1_sub1_2.desc.size == 1 && class30_sub2_sub4_sub1_sub1_2.x == player.x && class30_sub2_sub4_sub1_sub1_2.y == player.y) {
                            buildAtNpcMenu(class30_sub2_sub4_sub1_sub1_2.desc, localNpcIndices[k2], j1, i1);
                        }
                    }
                    for (int i3 = 0; i3 < localPlayerCount; i3++) {
                        Player class30_sub2_sub4_sub1_sub2_2 = localPlayers[playerIndices[i3]];
                        if (class30_sub2_sub4_sub1_sub2_2 != null && class30_sub2_sub4_sub1_sub2_2 != player && class30_sub2_sub4_sub1_sub2_2.x == player.x && class30_sub2_sub4_sub1_sub2_2.y == player.y) {
                            buildAtPlayerMenu(i1, playerIndices[i3], class30_sub2_sub4_sub1_sub2_2, j1);
                        }
                    }
                }
                buildAtPlayerMenu(i1, l1, player, j1);
            }
            if (k1 == 3) {
                Deque class19 = groundItems[plane][i1][j1];
                if (class19 != null) {
                    for (GroundItem groundItem = (GroundItem) class19.getBack(); groundItem != null; groundItem = (GroundItem) class19.getPrevious()) {
                        ItemDefinition itemDefinition = ItemDefinition.forId(groundItem.id);
                        if (itemSelected == 1) {
                            menuActionName[menuActionCount] = "Use " + selectedItemName + " with @lre@" + itemDefinition.name;
                            menuActionOpcode[menuActionCount] = 511;
                            menuActionCmd1[menuActionCount] = groundItem.id;
                            menuActionCmd2[menuActionCount] = i1;
                            menuActionCmd3[menuActionCount] = j1;
                            menuActionCount++;
                        } else if (spellSelected == 1) {
                            if ((spellUsableOn & 1) == 1) {
                                menuActionName[menuActionCount] = spellTooltip + " @lre@" + itemDefinition.name;
                                menuActionOpcode[menuActionCount] = 94;
                                menuActionCmd1[menuActionCount] = groundItem.id;
                                menuActionCmd2[menuActionCount] = i1;
                                menuActionCmd3[menuActionCount] = j1;
                                menuActionCount++;
                            }
                        } else {
                            for (int j3 = 4; j3 >= 0; j3--) {
                                if (itemDefinition.groundActions != null && itemDefinition.groundActions[j3] != null) {
                                    menuActionName[menuActionCount] = itemDefinition.groundActions[j3] + " @lre@" + itemDefinition.name;
                                    if (j3 == 0) {
                                        menuActionOpcode[menuActionCount] = 652;
                                    }
                                    if (j3 == 1) {
                                        menuActionOpcode[menuActionCount] = 567;
                                    }
                                    if (j3 == 2) {
                                        menuActionOpcode[menuActionCount] = 234;
                                    }
                                    if (j3 == 3) {
                                        menuActionOpcode[menuActionCount] = 244;
                                    }
                                    if (j3 == 4) {
                                        menuActionOpcode[menuActionCount] = 213;
                                    }
                                    menuActionCmd1[menuActionCount] = groundItem.id;
                                    menuActionCmd2[menuActionCount] = i1;
                                    menuActionCmd3[menuActionCount] = j1;
                                    menuActionCount++;
                                } else if (j3 == 2) {
                                    menuActionName[menuActionCount] = "Take @lre@" + itemDefinition.name;
                                    menuActionOpcode[menuActionCount] = 234;
                                    menuActionCmd1[menuActionCount] = groundItem.id;
                                    menuActionCmd2[menuActionCount] = i1;
                                    menuActionCmd3[menuActionCount] = j1;
                                    menuActionCount++;
                                }
                            }
                            menuActionName[menuActionCount] = "Examine @lre@" + itemDefinition.name;
                            menuActionOpcode[menuActionCount] = 1448;
                            menuActionCmd1[menuActionCount] = groundItem.id;
                            menuActionCmd2[menuActionCount] = i1;
                            menuActionCmd3[menuActionCount] = j1;
                            menuActionCount++;
                        }
                    }
                }
            }
        }
    }

    public void cleanUpForQuit() {
        Signlink.reporterror = false;
        try {
            if (socketStream != null) {
                socketStream.close();
            }
        } catch (Exception exception) {
        }
        socketStream = null;
        stopMidi();
        if (mouseRecorder != null) {
            mouseRecorder.running = false;
        }
        mouseRecorder = null;
        onDemandFetcher.dispose();
        onDemandFetcher = null;
        aStream_834 = null;
        outputStream = null;
        aStream_847 = null;
        inputStream = null;
        anIntArray1234 = null;
        aByteArrayArray1183 = null;
        aByteArrayArray1247 = null;
        anIntArray1235 = null;
        anIntArray1236 = null;
        intGroundArray = null;
        byteGroundArray = null;
        sceneGraph = null;
        collisionMaps = null;
        anIntArrayArray901 = null;
        anIntArrayArray825 = null;
        bigX = null;
        bigY = null;
        aByteArray912 = null;
        aGraphicsBuffer_1163 = null;
        minimapCanvas = null;
        gameScreenCanvas = null;
        aGraphicsBuffer_1166 = null;
        aGraphicsBuffer_1123 = null;
        aGraphicsBuffer_1124 = null;
        aGraphicsBuffer_1125 = null;
        backLeftIP1 = null;
        backLeftIP2 = null;
        backRightIP1 = null;
        backRightIP2 = null;
        backTopIP1 = null;
        backVmidIP1 = null;
        backVmidIP2 = null;
        backVmidIP3 = null;
        backVmidIP2_2 = null;
        invBack = null;
        mapBack = null;
        chatBack = null;
        backBase1 = null;
        backBase2 = null;
        backHmid1 = null;
        sideIcons = null;
        redStone1 = null;
        redStone2 = null;
        redStone3 = null;
        redStone1_2 = null;
        redStone2_2 = null;
        redStone1_3 = null;
        redStone2_3 = null;
        redStone3_2 = null;
        redStone1_4 = null;
        redStone2_4 = null;
        compass = null;
        hitMarks = null;
        skullIcon = null;
        prayerIcon = null;
        hintIcon = null;
        crosses = null;
        mapDotItem = null;
        mapDotNPC = null;
        mapDotPlayer = null;
        mapDotFriend = null;
        mapDotTeam = null;
        mapScenes = null;
        mapFunctions = null;
        anIntArrayArray929 = null;
        localPlayers = null;
        playerIndices = null;
        playerUpdateIndices = null;
        playerAppearanceBuffers = null;
        localPlayerIndices = null;
        localNpcs = null;
        localNpcIndices = null;
        groundItems = null;
        aClass19_1179 = null;
        projectileList = null;
        stillGraphicList = null;
        menuActionCmd2 = null;
        menuActionCmd3 = null;
        menuActionOpcode = null;
        menuActionCmd1 = null;
        menuActionName = null;
        configStates = null;
        anIntArray1072 = null;
        anIntArray1073 = null;
        aClass30_Sub2_Sub1_Sub1Array1140 = null;
        minimap = null;
        friendsNames = null;
        friendsNamesAsLongs = null;
        friendsWorlds = null;
        leftFlameCanvas = null;
        rightFlameCanvas = null;
        topLeftCanvas = null;
        bottomLeftCanvas = null;
        leftCanvas = null;
        titleBoxLeftCanvas = null;
        bottomRightCanvas = null;
        smallLeftCanvas = null;
        smallRightCanvas = null;
        dispose();
        ObjectDefinition.dispose();
        NpcDefinition.dispose();
        ItemDefinition.dispose();
        Floor.cachedFloors = null;
        IdentityKit.designCache = null;
        Interface.cachedInterfaces = null;
        Sequence.sequenceCache = null;
        SpotAnimation.spotAnimationCache = null;
        SpotAnimation.cache = null;
        Varp.cache = null;
        super.graphicsBuffer = null;
        Player.modelCache = null;
        Rasterizer.dispose();
        SceneGraph.dispose();
        Model.dispose();
        Animation.dispose();
        System.gc();
    }

    private void printDebug() {
        System.out.println("============");
        System.out.println("flame-cycle:" + flameCycle);
        if (onDemandFetcher != null) {
            System.out.println("Od-cycle:" + onDemandFetcher.onDemandCycle);
        }
        System.out.println("loop-cycle:" + Client.loopCycle);
        System.out.println("draw-cycle:" + Client.drawCycle);
        System.out.println("ptype:" + packetOpcode);
        System.out.println("psize:" + packetSize);
        if (socketStream != null) {
            socketStream.printDebug();
        }
        super.shouldDebug = true;
    }

    Component getGameComponent() {
        if (Signlink.mainapp != null) {
            return Signlink.mainapp;
        }
        if (super.frame != null) {
            return super.frame;
        } else {
            return this;
        }
    }

    private void processInput() { // method73
        do {
            int nextChar = getNextChar();
            if (nextChar == -1) {
                break;
            }
            if (openInterfaceId != -1 && openInterfaceId == reportAbuseInterfaceID) {
                if (nextChar == 8 && reportAbuseInput.length() > 0) {
                    reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
                }
                if ((nextChar >= 97 && nextChar <= 122 || nextChar >= 65 && nextChar <= 90 || nextChar >= 48 && nextChar <= 57 || nextChar == 32) && reportAbuseInput.length() < 12) {
                    reportAbuseInput += (char) nextChar;
                }
            } else if (messagePromptRaised) {
                if (nextChar >= 32 && nextChar <= 122 && promptInput.length() < 80) {
                    promptInput += (char) nextChar;
                    inputTaken = true;
                }
                if (nextChar == 8 && promptInput.length() > 0) {
                    promptInput = promptInput.substring(0, promptInput.length() - 1);
                    inputTaken = true;
                }
                if (nextChar == 13 || nextChar == 10) {
                    messagePromptRaised = false;
                    if (friendsListAction == 1) {
                        inputTaken = true;
                        long l = TextUtil.nameToLong(promptInput);
                        addFriend(l);
                    }
                    if (friendsListAction == 2 && friendsCount > 0) {
                        long l1 = TextUtil.nameToLong(promptInput);
                        delFriend(l1);
                    }
                    if (friendsListAction == 3 && promptInput.length() > 0) {
                        outputStream.writeOpcode(126);
                        outputStream.writeByte(0);
                        int k = outputStream.offset;
                        outputStream.writeLong(fromPlayer);
                        PlayerInput.packMessage(promptInput, outputStream);
                        outputStream.writeSizeByte(outputStream.offset - k);
                        promptInput = PlayerInput.processText(promptInput);
                        promptInput = Censor.doCensor(promptInput);
                        pushMessage(promptInput, 6, TextUtil.formatName(TextUtil.longToName(fromPlayer)));
                        if (privateChatMode == 2) {
                            privateChatMode = 1;
                            aBoolean1233 = true;
                            outputStream.writeOpcode(95);
                            outputStream.writeByte(publicChatMode);
                            outputStream.writeByte(privateChatMode);
                            outputStream.writeByte(tradeMode);
                        }
                    }
                    if (friendsListAction == 4 && ignoreCount < 100) {
                        long l2 = TextUtil.nameToLong(promptInput);
                        addIgnore(l2);
                    }
                    if (friendsListAction == 5 && ignoreCount > 0) {
                        long l3 = TextUtil.nameToLong(promptInput);
                        delIgnore(l3);
                    }
                }
            } else if (inputDialogState == 1) {
                if (nextChar >= 48 && nextChar <= 57 && amountOrNameInput.length() < 10) {
                    amountOrNameInput += (char) nextChar;
                    inputTaken = true;
                }
                if (nextChar == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                    inputTaken = true;
                }
                if (nextChar == 13 || nextChar == 10) {
                    if (amountOrNameInput.length() > 0) {
                        int i1 = 0;
                        try {
                            i1 = Integer.parseInt(amountOrNameInput);
                        } catch (Exception exception) {
                        }
                        outputStream.writeOpcode(208);
                        outputStream.writeInt(i1);
                    }
                    inputDialogState = 0;
                    inputTaken = true;
                }
            } else if (inputDialogState == 2) {
                if (nextChar >= 32 && nextChar <= 122 && amountOrNameInput.length() < 12) {
                    amountOrNameInput += (char) nextChar;
                    inputTaken = true;
                }
                if (nextChar == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                    inputTaken = true;
                }
                if (nextChar == 13 || nextChar == 10) {
                    if (amountOrNameInput.length() > 0) {
                        outputStream.writeOpcode(60);
                        outputStream.writeLong(TextUtil.nameToLong(amountOrNameInput));
                    }
                    inputDialogState = 0;
                    inputTaken = true;
                }
            } else if (backDialogID == -1) {
                if (nextChar >= 32 && nextChar <= 122 && inputString.length() < 80) {
                    inputString += (char) nextChar;
                    inputTaken = true;
                }
                if (nextChar == 8 && inputString.length() > 0) {
                    inputString = inputString.substring(0, inputString.length() - 1);
                    inputTaken = true;
                }
                if ((nextChar == 13 || nextChar == 10) && inputString.length() > 0) {
                    if (myPrivilege == 2) {
                        if (inputString.equals("::clientdrop")) {
                            dropClient();
                        }
                        if (inputString.equals("::lag")) {
                            printDebug();
                        }
                        if (inputString.equals("::prefetchmusic")) {
                            for (int j1 = 0; j1 < onDemandFetcher.getFileCount(2); j1++) {
                                onDemandFetcher.method563((byte) 1, 2, j1);
                            }
                        }
                        if (inputString.equals("::fpson")) {
                            Client.fpsIsOn = true;
                        }
                        if (inputString.equals("::fpsoff")) {
                            Client.fpsIsOn = false;
                        }
                        if (inputString.equals("::noclip")) {
                            for (int k1 = 0; k1 < 4; k1++) {
                                for (int i2 = 1; i2 < 103; i2++) {
                                    for (int k2 = 1; k2 < 103; k2++) {
                                        collisionMaps[k1].collisionFlags[i2][k2] = 0;
                                    }
                                }
                            }
                        }
                    }
                    if (inputString.startsWith("::")) {
                        outputStream.writeOpcode(103);
                        outputStream.writeByte(inputString.length() - 1);
                        outputStream.writeString(inputString.substring(2));
                    } else {
                        String s = inputString.toLowerCase();
                        int j2 = 0;
                        if (s.startsWith("yellow:")) {
                            j2 = 0;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("red:")) {
                            j2 = 1;
                            inputString = inputString.substring(4);
                        } else if (s.startsWith("green:")) {
                            j2 = 2;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("cyan:")) {
                            j2 = 3;
                            inputString = inputString.substring(5);
                        } else if (s.startsWith("purple:")) {
                            j2 = 4;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("white:")) {
                            j2 = 5;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("flash1:")) {
                            j2 = 6;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("flash2:")) {
                            j2 = 7;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("flash3:")) {
                            j2 = 8;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("glow1:")) {
                            j2 = 9;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("glow2:")) {
                            j2 = 10;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("glow3:")) {
                            j2 = 11;
                            inputString = inputString.substring(6);
                        }
                        s = inputString.toLowerCase();
                        int i3 = 0;
                        if (s.startsWith("wave:")) {
                            i3 = 1;
                            inputString = inputString.substring(5);
                        } else if (s.startsWith("wave2:")) {
                            i3 = 2;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("shake:")) {
                            i3 = 3;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("scroll:")) {
                            i3 = 4;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("slide:")) {
                            i3 = 5;
                            inputString = inputString.substring(6);
                        }
                        outputStream.writeOpcode(4);
                        outputStream.writeByte(0);
                        int j3 = outputStream.offset;
                        outputStream.writeByteS(i3);
                        outputStream.writeByteS(j2);
                        aStream_834.offset = 0;
                        PlayerInput.packMessage(inputString, aStream_834);
                        outputStream.writeCBytesA(0, aStream_834.payload, aStream_834.offset);
                        outputStream.writeSizeByte(outputStream.offset - j3);
                        inputString = PlayerInput.processText(inputString);
                        inputString = Censor.doCensor(inputString);
                        Client.myPlayer.textSpoken = inputString;
                        Client.myPlayer.textColor = j2;
                        Client.myPlayer.textEffects = i3;
                        Client.myPlayer.textCycle = 150;
                        if (myPrivilege == 2) {
                            pushMessage(Client.myPlayer.textSpoken, 2, "@cr2@" + Client.myPlayer.name);
                        } else if (myPrivilege == 1) {
                            pushMessage(Client.myPlayer.textSpoken, 2, "@cr1@" + Client.myPlayer.name);
                        } else {
                            pushMessage(Client.myPlayer.textSpoken, 2, Client.myPlayer.name);
                        }
                        if (publicChatMode == 2) {
                            publicChatMode = 3;
                            aBoolean1233 = true;
                            outputStream.writeOpcode(95);
                            outputStream.writeByte(publicChatMode);
                            outputStream.writeByte(privateChatMode);
                            outputStream.writeByte(tradeMode);
                        }
                    }
                    inputString = "";
                    inputTaken = true;
                }
            }
        } while (true);
    }

    private void buildChatAreaMenu(int arg0) {
        int i = 0;
        for (int id = 0; id < 100; id++) {
            if (chatMessages[id] == null) {
                continue;
            }
            int chatType = chatTypes[id];
            int scrollPos = 70 - i * 14 + chatboxScrollerPos + 4;
            if (scrollPos < -20) {
                break;
            }
            String name = chatNames[id];
            if (name != null && name.startsWith("@cr1@")) {
                name = name.substring(5);
            }
            if (name != null && name.startsWith("@cr2@")) {
                name = name.substring(5);
            }
            if (chatType == 0) {
                i++;
            }
            if ((chatType == 1 || chatType == 2) && (chatType == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(name))) {
                if (arg0 > scrollPos - 14 && arg0 <= scrollPos && !name.equals(Client.myPlayer.name)) {
                    if (myPrivilege >= 1) {
                        menuActionName[menuActionCount] = "Report abuse @whi@" + name;
                        menuActionOpcode[menuActionCount] = 606;
                        menuActionCount++;
                    }
                    menuActionName[menuActionCount] = "Add ignore @whi@" + name;
                    menuActionOpcode[menuActionCount] = 42;
                    menuActionCount++;
                    menuActionName[menuActionCount] = "Add friend @whi@" + name;
                    menuActionOpcode[menuActionCount] = 337;
                    menuActionCount++;
                }
                i++;
            }
            if ((chatType == 3 || chatType == 7) && splitPrivateChat == 0 && (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(name))) {
                if (arg0 > scrollPos - 14 && arg0 <= scrollPos) {
                    if (myPrivilege >= 1) {
                        menuActionName[menuActionCount] = "Report abuse @whi@" + name;
                        menuActionOpcode[menuActionCount] = 606;
                        menuActionCount++;
                    }
                    menuActionName[menuActionCount] = "Add ignore @whi@" + name;
                    menuActionOpcode[menuActionCount] = 42;
                    menuActionCount++;
                    menuActionName[menuActionCount] = "Add friend @whi@" + name;
                    menuActionOpcode[menuActionCount] = 337;
                    menuActionCount++;
                }
                i++;
            }
            if (chatType == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
                if (arg0 > scrollPos - 14 && arg0 <= scrollPos) {
                    menuActionName[menuActionCount] = "Accept trade @whi@" + name;
                    menuActionOpcode[menuActionCount] = 484;
                    menuActionCount++;
                }
                i++;
            }
            if ((chatType == 5 || chatType == 6) && splitPrivateChat == 0 && privateChatMode < 2) {
                i++;
            }
            if (chatType == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
                if (arg0 > scrollPos - 14 && arg0 <= scrollPos) {
                    menuActionName[menuActionCount] = "Accept challenge @whi@" + name;
                    menuActionOpcode[menuActionCount] = 6;
                    menuActionCount++;
                }
                i++;
            }
        }
    }

    private void drawLoginInterfaces(Interface component) { // drawFriendsListOrWelcomeScreen
        int j = component.contentType;
        if (j >= 1 && j <= 100 || j >= 701 && j <= 800) {
            if (j == 1 && anInt900 == 0) {
                component.message = "Loading friend list";
                component.actionType = 0;
                return;
            }
            if (j == 1 && anInt900 == 1) {
                component.message = "Connecting to friendserver";
                component.actionType = 0;
                return;
            }
            if (j == 2 && anInt900 != 2) {
                component.message = "Please wait...";
                component.actionType = 0;
                return;
            }
            int k = friendsCount;
            if (anInt900 != 2) {
                k = 0;
            }
            if (j > 700) {
                j -= 601;
            } else {
                j--;
            }
            if (j >= k) {
                component.message = "";
                component.actionType = 0;
                return;
            } else {
                component.message = friendsNames[j];
                component.actionType = 1;
                return;
            }
        }
        if (j >= 101 && j <= 200 || j >= 801 && j <= 900) {
            int l = friendsCount;
            if (anInt900 != 2) {
                l = 0;
            }
            if (j > 800) {
                j -= 701;
            } else {
                j -= 101;
            }
            if (j >= l) {
                component.message = "";
                component.actionType = 0;
                return;
            }
            if (friendsWorlds[j] == 0) {
                component.message = "@red@Offline";
            } else if (friendsWorlds[j] == Client.nodeID) {
                component.message = "@gre@World-" + (friendsWorlds[j] - 9);
            } else {
                component.message = "@yel@World-" + (friendsWorlds[j] - 9);
            }
            component.actionType = 1;
            return;
        }
        if (j == 203) {
            int i1 = friendsCount;
            if (anInt900 != 2) {
                i1 = 0;
            }
            component.scrollMax = i1 * 15 + 20;
            if (component.scrollMax <= component.height) {
                component.scrollMax = component.height + 1;
            }
            return;
        }
        if (j >= 401 && j <= 500) {
            if ((j -= 401) == 0 && anInt900 == 0) {
                component.message = "Loading ignore list";
                component.actionType = 0;
                return;
            }
            if (j == 1 && anInt900 == 0) {
                component.message = "Please wait...";
                component.actionType = 0;
                return;
            }
            int j1 = ignoreCount;
            if (anInt900 == 0) {
                j1 = 0;
            }
            if (j >= j1) {
                component.message = "";
                component.actionType = 0;
                return;
            } else {
                component.message = TextUtil.formatName(TextUtil.longToName(ignoreListAsLongs[j]));
                component.actionType = 1;
                return;
            }
        }
        if (j == 503) {
            component.scrollMax = ignoreCount * 15 + 20;
            if (component.scrollMax <= component.height) {
                component.scrollMax = component.height + 1;
            }
            return;
        }
        if (j == 327) {
            component.anInt270 = 150;
            component.anInt271 = (int) (Math.sin((double) Client.loopCycle / 40D) * 256D) & 0x7ff;
            if (aBoolean1031) {
                for (int k1 = 0; k1 < 7; k1++) {
                    int l1 = anIntArray1065[k1];
                    if (l1 >= 0 && !IdentityKit.designCache[l1].method537()) {
                        return;
                    }
                }
                aBoolean1031 = false;
                Model aclass30_sub2_sub4_sub6s[] = new Model[7];
                int i2 = 0;
                for (int j2 = 0; j2 < 7; j2++) {
                    int k2 = anIntArray1065[j2];
                    if (k2 >= 0) {
                        aclass30_sub2_sub4_sub6s[i2++] = IdentityKit.designCache[k2].method538();
                    }
                }
                Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
                for (int l2 = 0; l2 < 5; l2++) {
                    if (anIntArray990[l2] != 0) {
                        model.swapColors(Client.anIntArrayArray1003[l2][0], Client.anIntArrayArray1003[l2][anIntArray990[l2]]);
                        if (l2 == 1) {
                            model.swapColors(Client.anIntArray1204[0], Client.anIntArray1204[anIntArray990[l2]]);
                        }
                    }
                }
                model.skin();
                model.transform(Sequence.sequenceCache[Client.myPlayer.idleAnimation].frames[0]);
                model.processLighting(64, 850, -30, -50, -30, true);
                component.anInt233 = 5;
                component.mediaId = 0;
                Interface.method208(aBoolean994, model);
            }
            return;
        }
        if (j == 324) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = component.sprite1;
                aClass30_Sub2_Sub1_Sub1_932 = component.sprite2;
            }
            if (aBoolean1047) {
                component.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
                return;
            } else {
                component.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
                return;
            }
        }
        if (j == 325) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = component.sprite1;
                aClass30_Sub2_Sub1_Sub1_932 = component.sprite2;
            }
            if (aBoolean1047) {
                component.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
                return;
            } else {
                component.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
                return;
            }
        }
        if (j == 600) {
            component.message = reportAbuseInput;
            if (Client.loopCycle % 20 < 10) {
                component.message += "|";
                return;
            } else {
                component.message += " ";
                return;
            }
        }
        if (j == 613) {
            if (myPrivilege >= 1) {
                if (canMute) {
                    component.textColor = 0xff0000;
                    component.message = "Moderator option: Mute player for 48 hours: <ON>";
                } else {
                    component.textColor = 0xffffff;
                    component.message = "Moderator option: Mute player for 48 hours: <OFF>";
                }
            } else {
                component.message = "";
            }
        }
        if (j == 650 || j == 655) {
            if (anInt1193 != 0) {
                String s;
                if (daysSinceLastLogin == 0) {
                    s = "earlier today";
                } else if (daysSinceLastLogin == 1) {
                    s = "yesterday";
                } else {
                    s = daysSinceLastLogin + " days ago";
                }
                component.message = "You last logged in " + s + " from: " + Signlink.dns;
            } else {
                component.message = "";
            }
        }
        if (j == 651) {
            if (unreadMessages == 0) {
                component.message = "0 unread messages";
                component.textColor = 0xffff00;
            }
            if (unreadMessages == 1) {
                component.message = "1 unread message";
                component.textColor = 65280;
            }
            if (unreadMessages > 1) {
                component.message = unreadMessages + " unread messages";
                component.textColor = 65280;
            }
        }
        if (j == 652) {
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1) {
                    component.message = "@yel@This is a non-members world: @whi@Since you are a member we";
                } else {
                    component.message = "";
                }
            } else if (daysSinceRecovChange == 200) {
                component.message = "You have not yet set any password recovery questions.";
            } else {
                String s1;
                if (daysSinceRecovChange == 0) {
                    s1 = "Earlier today";
                } else if (daysSinceRecovChange == 1) {
                    s1 = "Yesterday";
                } else {
                    s1 = daysSinceRecovChange + " days ago";
                }
                component.message = s1 + " you changed your recovery questions";
            }
        }
        if (j == 653) {
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1) {
                    component.message = "@whi@recommend you use a members world instead. You may use";
                } else {
                    component.message = "";
                }
            } else if (daysSinceRecovChange == 200) {
                component.message = "We strongly recommend you do so now to secure your account.";
            } else {
                component.message = "If you do not remember making this change then cancel it immediately";
            }
        }
        if (j == 654) {
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1) {
                    component.message = "@whi@this world but member benefits are unavailable whilst here.";
                    return;
                } else {
                    component.message = "";
                    return;
                }
            }
            if (daysSinceRecovChange == 200) {
                component.message = "Do this from the 'account management' area on our front webpage";
                return;
            }
            component.message = "Do this from the 'account management' area on our front webpage";
        }
    }

    private void drawSplitPrivateChat() {
        if (splitPrivateChat == 0) {
            return;
        }
        Font font = aFont_1271;
        int i = 0;
        if (systemUpdateTime != 0) {
            i = 1;
        }
        for (int id = 0; id < 100; id++) {
            if (chatMessages[id] != null) {
                int chatType = chatTypes[id];
                String name = chatNames[id];
                byte rights = 0;
                if (name != null && name.startsWith("@cr1@")) {
                    name = name.substring(5);
                    rights = 1;
                }
                if (name != null && name.startsWith("@cr2@")) {
                    name = name.substring(5);
                    rights = 2;
                }
                if ((chatType == 3 || chatType == 7) && (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(name))) {
                    int y = 329 - i * 13;
                    int x = 4;
                    font.drawText(0, "From", y, x);
                    font.drawText(65535, "From", y - 1, x);
                    x += font.getWidth("From ");
                    if (rights == 1) {
                        modIcons[0].drawIndexedSprite(x, y - 12);
                        x += 14;
                    }
                    if (rights == 2) {
                        modIcons[1].drawIndexedSprite(x, y - 12);
                        x += 14;
                    }
                    font.drawText(0, name + ": " + chatMessages[id], y, x);
                    font.drawText(65535, name + ": " + chatMessages[id], y - 1, x);
                    if (++i >= 5) {
                        return;
                    }
                }
                if (chatType == 5 && privateChatMode < 2) {
                    int i1 = 329 - i * 13;
                    font.drawText(0, chatMessages[id], i1, 4);
                    font.drawText(65535, chatMessages[id], i1 - 1, 4);
                    if (++i >= 5) {
                        return;
                    }
                }
                if (chatType == 6 && privateChatMode < 2) {
                    int j1 = 329 - i * 13;
                    font.drawText(0, "To " + name + ": " + chatMessages[id], j1, 4);
                    font.drawText(65535, "To " + name + ": " + chatMessages[id], j1 - 1, 4);
                    if (++i >= 5) {
                        return;
                    }
                }
            }
        }
    }

    private void pushMessage(String text, int inputId, String name) { // pushMessage
        if (inputId == 0 && dialogID != -1) {
            aString844 = text;
            super.clickMode3 = 0;
        }
        if (backDialogID == -1) {
            inputTaken = true;
        }
        for (int j = 99; j > 0; j--) {
            chatTypes[j] = chatTypes[j - 1];
            chatNames[j] = chatNames[j - 1];
            chatMessages[j] = chatMessages[j - 1];
        }
        chatTypes[0] = inputId;
        chatNames[0] = name;
        chatMessages[0] = text;
    }

    private void processTabClick() {
        if (super.clickMode3 == 1) {
            if (super.saveClickX >= 539 && super.saveClickX <= 573 && super.saveClickY >= 169 && super.saveClickY < 205 && tabInterfaceIDs[0] != -1) {
                needDrawTabArea = true;
                tabID = 0;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 569 && super.saveClickX <= 599 && super.saveClickY >= 168 && super.saveClickY < 205 && tabInterfaceIDs[1] != -1) {
                needDrawTabArea = true;
                tabID = 1;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 597 && super.saveClickX <= 627 && super.saveClickY >= 168 && super.saveClickY < 205 && tabInterfaceIDs[2] != -1) {
                needDrawTabArea = true;
                tabID = 2;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 625 && super.saveClickX <= 669 && super.saveClickY >= 168 && super.saveClickY < 203 && tabInterfaceIDs[3] != -1) {
                needDrawTabArea = true;
                tabID = 3;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 666 && super.saveClickX <= 696 && super.saveClickY >= 168 && super.saveClickY < 205 && tabInterfaceIDs[4] != -1) {
                needDrawTabArea = true;
                tabID = 4;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 694 && super.saveClickX <= 724 && super.saveClickY >= 168 && super.saveClickY < 205 && tabInterfaceIDs[5] != -1) {
                needDrawTabArea = true;
                tabID = 5;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 722 && super.saveClickX <= 756 && super.saveClickY >= 169 && super.saveClickY < 205 && tabInterfaceIDs[6] != -1) {
                needDrawTabArea = true;
                tabID = 6;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 540 && super.saveClickX <= 574 && super.saveClickY >= 466 && super.saveClickY < 502 && tabInterfaceIDs[7] != -1) {
                needDrawTabArea = true;
                tabID = 7;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 572 && super.saveClickX <= 602 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[8] != -1) {
                needDrawTabArea = true;
                tabID = 8;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 599 && super.saveClickX <= 629 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[9] != -1) {
                needDrawTabArea = true;
                tabID = 9;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 627 && super.saveClickX <= 671 && super.saveClickY >= 467 && super.saveClickY < 502 && tabInterfaceIDs[10] != -1) {
                needDrawTabArea = true;
                tabID = 10;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 669 && super.saveClickX <= 699 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[11] != -1) {
                needDrawTabArea = true;
                tabID = 11;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 696 && super.saveClickX <= 726 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[12] != -1) {
                needDrawTabArea = true;
                tabID = 12;
                tabAreaAltered = true;
            }
            if (super.saveClickX >= 724 && super.saveClickX <= 758 && super.saveClickY >= 466 && super.saveClickY < 502 && tabInterfaceIDs[13] != -1) {
                needDrawTabArea = true;
                tabID = 13;
                tabAreaAltered = true;
            }
        }
    }

    private void resetImageProducers2() {
        if (aGraphicsBuffer_1166 != null) {
            return;
        }
        dispose();
        super.graphicsBuffer = null;
        topLeftCanvas = null;
        bottomLeftCanvas = null;
        leftCanvas = null;
        leftFlameCanvas = null;
        rightFlameCanvas = null;
        titleBoxLeftCanvas = null;
        bottomRightCanvas = null;
        smallLeftCanvas = null;
        smallRightCanvas = null;
        aGraphicsBuffer_1166 = new GraphicsBuffer(479, 96, getGameComponent());
        minimapCanvas = new GraphicsBuffer(172, 156, getGameComponent());
        Graphics2D.resetPixels();
        mapBack.drawIndexedSprite(0, 0);
        aGraphicsBuffer_1163 = new GraphicsBuffer(190, 261, getGameComponent());
        gameScreenCanvas = new GraphicsBuffer(512, 334, getGameComponent());
        Graphics2D.resetPixels();
        aGraphicsBuffer_1123 = new GraphicsBuffer(496, 50, getGameComponent());
        aGraphicsBuffer_1124 = new GraphicsBuffer(269, 37, getGameComponent());
        aGraphicsBuffer_1125 = new GraphicsBuffer(249, 45, getGameComponent());
        welcomeScreenRaised = true;
    }

    private String getHost() {
        if (Signlink.mainapp != null) {
            return Signlink.mainapp.getDocumentBase().getHost().toLowerCase();
        }
        if (super.frame != null) {
            return "runescape.com";
        } else {
            return super.getDocumentBase().getHost().toLowerCase();
        }
    }

    private void method81(Sprite sprite, int y, int x) {
        int bounds = x * x + y * y;
        if (bounds > 4225 && bounds < 90000) {
            int i1 = cameraX + minimapInt2 & 0x7ff;
            int j1 = Model.modelIntArray1[i1];
            int k1 = Model.modelIntArray2[i1];
            j1 = j1 * 256 / (minimapInt3 + 256);
            k1 = k1 * 256 / (minimapInt3 + 256);
            int l1 = y * j1 + x * k1 >> 16;
            int i2 = y * k1 - x * j1 >> 16;
            double d = Math.atan2(l1, i2);
            int j2 = (int) (Math.sin(d) * 63D);
            int k2 = (int) (Math.cos(d) * 57D);
            mapEdge.drawRotatedSprite(83 - k2 - 20, d, 94 + j2 + 4 - 10);
        } else {
            markMinimap(sprite, x, y);
        }
    }

    private void processRightClick() {
        if (activeInterfaceType != 0) {
            return;
        }
        menuActionName[0] = "Cancel";
        menuActionOpcode[0] = 1107;
        menuActionCount = 1;
        buildSplitPrivateChatMenu();
        anInt886 = 0;
        if (super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516 && super.mouseY < 338) {
            if (openInterfaceId != -1) {
                buildInterfaceMenu(4, Interface.cachedInterfaces[openInterfaceId], super.mouseX, 4, super.mouseY, 0);
            } else {
                build3dScreenMenu();
            }
        }
        if (anInt886 != anInt1026) {
            anInt1026 = anInt886;
        }
        anInt886 = 0;
        if (super.mouseX > 553 && super.mouseY > 205 && super.mouseX < 743 && super.mouseY < 466) {
            if (invOverlayInterfaceID != -1) {
                buildInterfaceMenu(553, Interface.cachedInterfaces[invOverlayInterfaceID], super.mouseX, 205, super.mouseY, 0);
            } else if (tabInterfaceIDs[tabID] != -1) {
                buildInterfaceMenu(553, Interface.cachedInterfaces[tabInterfaceIDs[tabID]], super.mouseX, 205, super.mouseY, 0);
            }
        }
        if (anInt886 != anInt1048) {
            needDrawTabArea = true;
            anInt1048 = anInt886;
        }
        anInt886 = 0;
        if (super.mouseX > 17 && super.mouseY > 357 && super.mouseX < 496 && super.mouseY < 453) {
            if (backDialogID != -1) {
                buildInterfaceMenu(17, Interface.cachedInterfaces[backDialogID], super.mouseX, 357, super.mouseY, 0);
            } else if (super.mouseY < 434 && super.mouseX < 426) {
                buildChatAreaMenu(super.mouseY - 357);
            }
        }
        if (backDialogID != -1 && anInt886 != anInt1039) {
            inputTaken = true;
            anInt1039 = anInt886;
        }
        boolean flag = false;
        while (!flag) {
            flag = true;
            for (int j = 0; j < menuActionCount - 1; j++) {
                if (menuActionOpcode[j] < 1000 && menuActionOpcode[j + 1] > 1000) {
                    String s = menuActionName[j];
                    menuActionName[j] = menuActionName[j + 1];
                    menuActionName[j + 1] = s;
                    int k = menuActionOpcode[j];
                    menuActionOpcode[j] = menuActionOpcode[j + 1];
                    menuActionOpcode[j + 1] = k;
                    k = menuActionCmd2[j];
                    menuActionCmd2[j] = menuActionCmd2[j + 1];
                    menuActionCmd2[j + 1] = k;
                    k = menuActionCmd3[j];
                    menuActionCmd3[j] = menuActionCmd3[j + 1];
                    menuActionCmd3[j + 1] = k;
                    k = menuActionCmd1[j];
                    menuActionCmd1[j] = menuActionCmd1[j + 1];
                    menuActionCmd1[j + 1] = k;
                    flag = false;
                }
            }
        }
    }

    private int method83(int arg0, int arg1, int arg2) {
        int l = 256 - arg2;
        return ((arg0 & 0xff00ff) * l + (arg1 & 0xff00ff) * arg2 & 0xff00ff00) + ((arg0 & 0xff00) * l + (arg1 & 0xff00) * arg2 & 0xff0000) >> 8;
    }

    private void login(String arg0, String arg1, boolean flag) {
        Signlink.errorname = arg0;
        try {
            if (!flag) {
                loginMessage1 = "";
                loginMessage2 = "Connecting to server...";
                drawTitlebox(true);
            }
            socketStream = new Socket(this, openSocket(43594 + Client.portOff));
            long l = TextUtil.nameToLong(arg0);
            int i = (int) (l >> 16 & 31L);
            outputStream.offset = 0;
            outputStream.writeByte(14);
            outputStream.writeByte(i);
            socketStream.queueBytes(2, outputStream.payload);
            for (int j = 0; j < 8; j++) {
                socketStream.read();
            }
            int k = socketStream.read();
            int i1 = k;
            if (k == 0) {
                socketStream.flushInputStream(inputStream.payload, 8);
                inputStream.offset = 0;
                aLong1215 = inputStream.getLong();
                int ai[] = new int[4];
                ai[0] = (int) (Math.random() * 99999999D);
                ai[1] = (int) (Math.random() * 99999999D);
                ai[2] = (int) (aLong1215 >> 32);
                ai[3] = (int) aLong1215;
                outputStream.offset = 0;
                outputStream.writeByte(10);
                outputStream.writeInt(ai[0]);
                outputStream.writeInt(ai[1]);
                outputStream.writeInt(ai[2]);
                outputStream.writeInt(ai[3]);
                outputStream.writeInt(Signlink.uid);
                outputStream.writeString(arg0);
                outputStream.writeString(arg1);
                outputStream.encodeRsa();
                aStream_847.offset = 0;
                if (flag) {
                    aStream_847.writeByte(18);
                } else {
                    aStream_847.writeByte(16);
                }
                aStream_847.writeByte(outputStream.offset + 36 + 1 + 1 + 2);
                aStream_847.writeByte(255);
                aStream_847.writeShort(317);
                aStream_847.writeByte(Client.lowMem ? 1 : 0);
                for (int l1 = 0; l1 < 9; l1++) {
                    aStream_847.writeInt(expectedCRCs[l1]);
                }
                aStream_847.writeBytes(outputStream.payload, outputStream.offset, 0);
                outputStream.cryption = new IsaacCipher(ai);
                for (int j2 = 0; j2 < 4; j2++) {
                    ai[j2] += 50;
                }
                encryption = new IsaacCipher(ai);
                socketStream.queueBytes(aStream_847.offset, aStream_847.payload);
                k = socketStream.read();
            }
            if (k == 1) {
                try {
                    Thread.sleep(2000L);
                } catch (Exception exception) {
                }
                login(arg0, arg1, flag);
                return;
            }
            if (k == 2) {
                myPrivilege = socketStream.read();
                Client.flagged = socketStream.read() == 1;
                aLong1220 = 0L;
                anInt1022 = 0;
                mouseRecorder.cacheIndex = 0;
                super.awtFocus = true;
                aBoolean954 = true;
                loggedIn = true;
                outputStream.offset = 0;
                inputStream.offset = 0;
                packetOpcode = -1;
                anInt841 = -1;
                anInt842 = -1;
                anInt843 = -1;
                packetSize = 0;
                anInt1009 = 0;
                systemUpdateTime = 0;
                anInt1011 = 0;
                anInt855 = 0;
                menuActionCount = 0;
                menuOpen = false;
                super.idleTime = 0;
                for (int j1 = 0; j1 < 100; j1++) {
                    chatMessages[j1] = null;
                }
                itemSelected = 0;
                spellSelected = 0;
                loadingStage = 0;
                anInt1062 = 0;
                anInt1278 = (int) (Math.random() * 100D) - 50;
                anInt1131 = (int) (Math.random() * 110D) - 55;
                anInt896 = (int) (Math.random() * 80D) - 40;
                minimapInt2 = (int) (Math.random() * 120D) - 60;
                minimapInt3 = (int) (Math.random() * 30D) - 20;
                cameraX = (int) (Math.random() * 20D) - 10 & 0x7ff;
                anInt1021 = 0;
                anInt985 = -1;
                destX = 0;
                destY = 0;
                localPlayerCount = 0;
                localNpcCount = 0;
                for (int i2 = 0; i2 < maxPlayers; i2++) {
                    localPlayers[i2] = null;
                    playerAppearanceBuffers[i2] = null;
                }
                for (int k2 = 0; k2 < 16384; k2++) {
                    localNpcs[k2] = null;
                }
                Client.myPlayer = localPlayers[myPlayerIndex] = new Player();
                projectileList.clear();
                stillGraphicList.clear();
                for (int l2 = 0; l2 < 4; l2++) {
                    for (int i3 = 0; i3 < 104; i3++) {
                        for (int k3 = 0; k3 < 104; k3++) {
                            groundItems[l2][i3][k3] = null;
                        }
                    }
                }
                aClass19_1179 = new Deque();
                anInt900 = 0;
                friendsCount = 0;
                dialogID = -1;
                backDialogID = -1;
                openInterfaceId = -1;
                invOverlayInterfaceID = -1;
                anInt1018 = -1;
                aBoolean1149 = false;
                tabID = 3;
                inputDialogState = 0;
                menuOpen = false;
                messagePromptRaised = false;
                aString844 = null;
                anInt1055 = 0;
                anInt1054 = -1;
                aBoolean1047 = true;
                method45();
                for (int j3 = 0; j3 < 5; j3++) {
                    anIntArray990[j3] = 0;
                }
                for (int l3 = 0; l3 < 5; l3++) {
                    atPlayerActions[l3] = null;
                    atPlayerArray[l3] = false;
                }
                Client.anInt1175 = 0;
                Client.anInt1134 = 0;
                Client.anInt986 = 0;
                Client.anInt1288 = 0;
                Client.anInt924 = 0;
                Client.anInt1188 = 0;
                Client.anInt1155 = 0;
                Client.anInt1226 = 0;
                resetImageProducers2();
                return;
            }
            if (k == 3) {
                loginMessage1 = "";
                loginMessage2 = "Invalid username or password.";
                return;
            }
            if (k == 4) {
                loginMessage1 = "Your account has been disabled.";
                loginMessage2 = "Please check your message-center for details.";
                return;
            }
            if (k == 5) {
                loginMessage1 = "Your account is already logged in.";
                loginMessage2 = "Try again in 60 secs...";
                return;
            }
            if (k == 6) {
                loginMessage1 = "RuneScape has been updated!";
                loginMessage2 = "Please reload this page.";
                return;
            }
            if (k == 7) {
                loginMessage1 = "This world is full.";
                loginMessage2 = "Please use a different world.";
                return;
            }
            if (k == 8) {
                loginMessage1 = "Unable to connect.";
                loginMessage2 = "Login server offline.";
                return;
            }
            if (k == 9) {
                loginMessage1 = "Login limit exceeded.";
                loginMessage2 = "Too many connections from your address.";
                return;
            }
            if (k == 10) {
                loginMessage1 = "Unable to connect.";
                loginMessage2 = "Bad session id.";
                return;
            }
            if (k == 11) {
                loginMessage2 = "Login server rejected session.";
                loginMessage2 = "Please try again.";
                return;
            }
            if (k == 12) {
                loginMessage1 = "You need a members account to login to this world.";
                loginMessage2 = "Please subscribe, or use a different world.";
                return;
            }
            if (k == 13) {
                loginMessage1 = "Could not complete login.";
                loginMessage2 = "Please try using a different world.";
                return;
            }
            if (k == 14) {
                loginMessage1 = "The server is being updated.";
                loginMessage2 = "Please wait 1 minute and try again.";
                return;
            }
            if (k == 15) {
                loggedIn = true;
                outputStream.offset = 0;
                inputStream.offset = 0;
                packetOpcode = -1;
                anInt841 = -1;
                anInt842 = -1;
                anInt843 = -1;
                packetSize = 0;
                anInt1009 = 0;
                systemUpdateTime = 0;
                menuActionCount = 0;
                menuOpen = false;
                aLong824 = System.currentTimeMillis();
                return;
            }
            if (k == 16) {
                loginMessage1 = "Login attempts exceeded.";
                loginMessage2 = "Please wait 1 minute and try again.";
                return;
            }
            if (k == 17) {
                loginMessage1 = "You are standing in a members-only area.";
                loginMessage2 = "To play on this world move to a free area first";
                return;
            }
            if (k == 20) {
                loginMessage1 = "Invalid loginserver requested";
                loginMessage2 = "Please try using a different world.";
                return;
            }
            if (k == 21) {
                for (int k1 = socketStream.read(); k1 >= 0; k1--) {
                    loginMessage1 = "You have only just left another world";
                    loginMessage2 = "Your profile will be transferred in: " + k1 + " seconds";
                    drawTitlebox(true);
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception exception) {
                    }
                }
                login(arg0, arg1, flag);
                return;
            }
            if (k == -1) {
                if (i1 == 0) {
                    if (loginFailures < 2) {
                        try {
                            Thread.sleep(2000L);
                        } catch (Exception exception) {
                        }
                        loginFailures++;
                        login(arg0, arg1, flag);
                        return;
                    } else {
                        loginMessage1 = "No response from loginserver";
                        loginMessage2 = "Please wait 1 minute and try again.";
                        return;
                    }
                } else {
                    loginMessage1 = "No response from server";
                    loginMessage2 = "Please try using a different world.";
                    return;
                }
            } else {
                System.out.println("response:" + k);
                loginMessage1 = "Unexpected server response";
                loginMessage2 = "Please try using a different world.";
                return;
            }
        } catch (IOException exception) {
            loginMessage1 = "";
        }
        loginMessage2 = "Error connecting to server.";
    }

    private boolean doWalkTo(int walkType, int rotation, int arg2, int objectType, int srcY, int arg5, int arg6, int destY, int srcX, boolean flag, int destX) {
        byte x104 = 104;
        byte y104 = 104;
        for (int base104X = 0; base104X < x104; base104X++) {
            for (int base104Y = 0; base104Y < y104; base104Y++) {
                anIntArrayArray901[base104X][base104Y] = 0;
                anIntArrayArray825[base104X][base104Y] = 0x5f5e0ff;
            }
        }
        int srcOffX = srcX;
        int srcOffY = srcY;
        anIntArrayArray901[srcX][srcY] = 99;
        anIntArrayArray825[srcX][srcY] = 0;
        int head = 0;
        int tail = 0;
        bigX[head] = srcX;
        bigY[head++] = srcY;
        boolean reachedDest = false;
        int maxSteps = bigX.length;
        int collisionFlags[][] = collisionMaps[plane].collisionFlags;
        while (tail != head) {
            srcOffX = bigX[tail];
            srcOffY = bigY[tail];
            tail = (tail + 1) % maxSteps;
            if (srcOffX == destX && srcOffY == destY) {
                reachedDest = true;
                break;
            }
            if (objectType != 0) {
                if ((objectType < 5 || objectType == 10) && collisionMaps[plane].method219(destX, srcOffX, srcOffY, rotation, objectType - 1, destY)) {
                    reachedDest = true;
                    break;
                }
                if (objectType < 10 && collisionMaps[plane].method220(destX, destY, srcOffY, objectType - 1, rotation, srcOffX)) {
                    reachedDest = true;
                    break;
                }
            }
            if (arg5 != 0 && arg2 != 0 && collisionMaps[plane].method221(destY, destX, srcOffX, arg2, arg6, arg5, srcOffY)) {
                reachedDest = true;
                break;
            }
            int l4 = anIntArrayArray825[srcOffX][srcOffY] + 1;
            if (srcOffX > 0 && anIntArrayArray901[srcOffX - 1][srcOffY] == 0 && (collisionFlags[srcOffX - 1][srcOffY] & 0x1280108) == 0) {
                bigX[head] = srcOffX - 1;
                bigY[head] = srcOffY;
                head = (head + 1) % maxSteps;
                anIntArrayArray901[srcOffX - 1][srcOffY] = 2;
                anIntArrayArray825[srcOffX - 1][srcOffY] = l4;
            }
            if (srcOffX < x104 - 1 && anIntArrayArray901[srcOffX + 1][srcOffY] == 0 && (collisionFlags[srcOffX + 1][srcOffY] & 0x1280180) == 0) {
                bigX[head] = srcOffX + 1;
                bigY[head] = srcOffY;
                head = (head + 1) % maxSteps;
                anIntArrayArray901[srcOffX + 1][srcOffY] = 8;
                anIntArrayArray825[srcOffX + 1][srcOffY] = l4;
            }
            if (srcOffY > 0 && anIntArrayArray901[srcOffX][srcOffY - 1] == 0 && (collisionFlags[srcOffX][srcOffY - 1] & 0x1280102) == 0) {
                bigX[head] = srcOffX;
                bigY[head] = srcOffY - 1;
                head = (head + 1) % maxSteps;
                anIntArrayArray901[srcOffX][srcOffY - 1] = 1;
                anIntArrayArray825[srcOffX][srcOffY - 1] = l4;
            }
            if (srcOffY < y104 - 1 && anIntArrayArray901[srcOffX][srcOffY + 1] == 0 && (collisionFlags[srcOffX][srcOffY + 1] & 0x1280120) == 0) {
                bigX[head] = srcOffX;
                bigY[head] = srcOffY + 1;
                head = (head + 1) % maxSteps;
                anIntArrayArray901[srcOffX][srcOffY + 1] = 4;
                anIntArrayArray825[srcOffX][srcOffY + 1] = l4;
            }
            if (srcOffX > 0 && srcOffY > 0 && anIntArrayArray901[srcOffX - 1][srcOffY - 1] == 0 && (collisionFlags[srcOffX - 1][srcOffY - 1] & 0x128010e) == 0 && (collisionFlags[srcOffX - 1][srcOffY] & 0x1280108) == 0 && (collisionFlags[srcOffX][srcOffY - 1] & 0x1280102) == 0) {
                bigX[head] = srcOffX - 1;
                bigY[head] = srcOffY - 1;
                head = (head + 1) % maxSteps;
                anIntArrayArray901[srcOffX - 1][srcOffY - 1] = 3;
                anIntArrayArray825[srcOffX - 1][srcOffY - 1] = l4;
            }
            if (srcOffX < x104 - 1 && srcOffY > 0 && anIntArrayArray901[srcOffX + 1][srcOffY - 1] == 0 && (collisionFlags[srcOffX + 1][srcOffY - 1] & 0x1280183) == 0 && (collisionFlags[srcOffX + 1][srcOffY] & 0x1280180) == 0 && (collisionFlags[srcOffX][srcOffY - 1] & 0x1280102) == 0) {
                bigX[head] = srcOffX + 1;
                bigY[head] = srcOffY - 1;
                head = (head + 1) % maxSteps;
                anIntArrayArray901[srcOffX + 1][srcOffY - 1] = 9;
                anIntArrayArray825[srcOffX + 1][srcOffY - 1] = l4;
            }
            if (srcOffX > 0 && srcOffY < y104 - 1 && anIntArrayArray901[srcOffX - 1][srcOffY + 1] == 0 && (collisionFlags[srcOffX - 1][srcOffY + 1] & 0x1280138) == 0 && (collisionFlags[srcOffX - 1][srcOffY] & 0x1280108) == 0 && (collisionFlags[srcOffX][srcOffY + 1] & 0x1280120) == 0) {
                bigX[head] = srcOffX - 1;
                bigY[head] = srcOffY + 1;
                head = (head + 1) % maxSteps;
                anIntArrayArray901[srcOffX - 1][srcOffY + 1] = 6;
                anIntArrayArray825[srcOffX - 1][srcOffY + 1] = l4;
            }
            if (srcOffX < x104 - 1 && srcOffY < y104 - 1 && anIntArrayArray901[srcOffX + 1][srcOffY + 1] == 0 && (collisionFlags[srcOffX + 1][srcOffY + 1] & 0x12801e0) == 0 && (collisionFlags[srcOffX + 1][srcOffY] & 0x1280180) == 0 && (collisionFlags[srcOffX][srcOffY + 1] & 0x1280120) == 0) {
                bigX[head] = srcOffX + 1;
                bigY[head] = srcOffY + 1;
                head = (head + 1) % maxSteps;
                anIntArrayArray901[srcOffX + 1][srcOffY + 1] = 12;
                anIntArrayArray825[srcOffX + 1][srcOffY + 1] = l4;
            }
        }
        anInt1264 = 0;
        if (!reachedDest) {
            if (flag) {
                int i5 = 100;
                for (int k5 = 1; k5 < 2; k5++) {
                    for (int i6 = destX - k5; i6 <= destX + k5; i6++) {
                        for (int l6 = destY - k5; l6 <= destY + k5; l6++) {
                            if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && anIntArrayArray825[i6][l6] < i5) {
                                i5 = anIntArrayArray825[i6][l6];
                                srcOffX = i6;
                                srcOffY = l6;
                                anInt1264 = 1;
                                reachedDest = true;
                            }
                        }
                    }
                    if (reachedDest) {
                        break;
                    }
                }
            }
            if (!reachedDest) {
                return false;
            }
        }
        tail = 0;
        bigX[tail] = srcOffX;
        bigY[tail++] = srcOffY;
        int l5;
        for (int j5 = l5 = anIntArrayArray901[srcOffX][srcOffY]; srcOffX != srcX || srcOffY != srcY; j5 = anIntArrayArray901[srcOffX][srcOffY]) {
            if (j5 != l5) {
                l5 = j5;
                bigX[tail] = srcOffX;
                bigY[tail++] = srcOffY;
            }
            if ((j5 & 2) != 0) {
                srcOffX++;
            } else if ((j5 & 8) != 0) {
                srcOffX--;
            }
            if ((j5 & 1) != 0) {
                srcOffY++;
            } else if ((j5 & 4) != 0) {
                srcOffY--;
            }
        }
        if (tail > 0) {
            int finalSteps = tail;
            if (finalSteps > 25) {
                finalSteps = 25;
            }
            tail--;
            int k6 = bigX[tail];
            int i7 = bigY[tail];
            Client.anInt1288 += finalSteps;
            if (Client.anInt1288 >= 92) {
                outputStream.writeOpcode(36);
                outputStream.writeInt(0);
                Client.anInt1288 = 0;
            }
            if (walkType == 0) {
                outputStream.writeOpcode(164);
                outputStream.writeByte(finalSteps + finalSteps + 3);
            }
            if (walkType == 1) {
                outputStream.writeOpcode(248);
                outputStream.writeByte(finalSteps + finalSteps + 3 + 14);
            }
            if (walkType == 2) {
                outputStream.writeOpcode(98);
                outputStream.writeByte(finalSteps + finalSteps + 3);
            }
            outputStream.writeLEShortA(k6 + baseX);
            this.destX = bigX[0];
            this.destY = bigY[0];
            for (int j7 = 1; j7 < finalSteps; j7++) {
                tail--;
                outputStream.writeByte(bigX[tail] - k6);
                outputStream.writeByte(bigY[tail] - i7);
            }
            outputStream.writeLEShort(i7 + baseY);
            outputStream.writeByteC(super.heldKeys[5] != 1 ? 0 : 1);
            return true;
        }
        return walkType != 1;
    }

    private void parseNpcUpdateFlags(Stream stream) { // method86
        for (int id = 0; id < entityUpdateCount; id++) {
            int playerId = playerUpdateIndices[id];
            Npc npc = localNpcs[playerId];
            int l = stream.getUnsignedByte();
            if ((l & 0x10) != 0) {
                int i1 = stream.getUnsignedLEShort();
                if (i1 == 65535) {
                    i1 = -1;
                }
                int delay = stream.getUnsignedByte();
                if (i1 == npc.animationId && i1 != -1) {
                    int l2 = Sequence.sequenceCache[i1].anInt365;
                    if (l2 == 1) {
                        npc.anInt1527 = 0;
                        npc.anInt1528 = 0;
                        npc.animationDelay = delay;
                        npc.anInt1530 = 0;
                    }
                    if (l2 == 2) {
                        npc.anInt1530 = 0;
                    }
                } else if (i1 == -1 || npc.animationId == -1 || Sequence.sequenceCache[i1].anInt359 >= Sequence.sequenceCache[npc.animationId].anInt359) {
                    npc.animationId = i1;
                    npc.anInt1527 = 0;
                    npc.anInt1528 = 0;
                    npc.animationDelay = delay;
                    npc.anInt1530 = 0;
                    npc.anInt1542 = npc.walkQueueLocationIndex;
                }
            }
            if ((l & 8) != 0) {
                int j1 = stream.getUnsignedByteA();
                int j2 = stream.getUnsignedByteC();
                npc.addHit(j2, j1, Client.loopCycle);
                npc.combatCycle = Client.loopCycle + 300;
                npc.currentHealth = stream.getUnsignedByteA();
                npc.maxHealth = stream.getUnsignedByte();
            }
            if ((l & 0x80) != 0) {
                npc.currentGraphic = stream.getUnsignedShort();
                int k1 = stream.getInt();
                npc.anInt1524 = k1 >> 16;
                npc.anInt1523 = Client.loopCycle + (k1 & 0xffff);
                npc.anInt1521 = 0;
                npc.anInt1522 = 0;
                if (npc.anInt1523 > Client.loopCycle) {
                    npc.anInt1521 = -1;
                }
                if (npc.currentGraphic == 65535) {
                    npc.currentGraphic = -1;
                }
            }
            if ((l & 0x20) != 0) {
                npc.interactingEntity = stream.getUnsignedShort();
                if (npc.interactingEntity == 65535) {
                    npc.interactingEntity = -1;
                }
            }
            if ((l & 1) != 0) {
                npc.textSpoken = stream.getString();
                npc.textCycle = 100;
            }
            if ((l & 0x40) != 0) {
                int l1 = stream.getUnsignedByteC();
                int k2 = stream.getUnsignedByteS();
                npc.addHit(k2, l1, Client.loopCycle);
                npc.combatCycle = Client.loopCycle + 300;
                npc.currentHealth = stream.getUnsignedByteS();
                npc.maxHealth = stream.getUnsignedByteC();
            }
            if ((l & 2) != 0) {
                npc.desc = NpcDefinition.forId(stream.getUnsignedShortA());
                npc.anInt1540 = npc.desc.size;
                npc.defaultTurnValue = npc.desc.turnAmount;
                npc.walkAnimation = npc.desc.walkAnim;
                npc.anInt1555 = npc.desc.retreatAnim;
                npc.anInt1556 = npc.desc.turnRightAnim;
                npc.anInt1557 = npc.desc.turnLeftAnim;
                npc.idleAnimation = npc.desc.idleAnim;
            }
            if ((l & 4) != 0) {
                npc.anInt1538 = stream.getUnsignedLEShort();
                npc.anInt1539 = stream.getUnsignedLEShort();
            }
        }
    }

    private void buildAtNpcMenu(NpcDefinition npc, int option1, int option3, int option2) {
        if (menuActionCount >= 400) {
            return;
        }
        if (npc.childIds != null) {
            npc = npc.method161();
        }
        if (npc == null) {
            return;
        }
        if (!npc.clickable) {
            return;
        }
        String s = npc.name;
        if (npc.combatLevel != 0) {
            s = s + Client.getCombatLevelColor(Client.myPlayer.combatLevel, npc.combatLevel) + " (level-" + npc.combatLevel + ")";
        }
        if (itemSelected == 1) {
            menuActionName[menuActionCount] = "Use " + selectedItemName + " with @yel@" + s;
            menuActionOpcode[menuActionCount] = 582;
            menuActionCmd1[menuActionCount] = option1;
            menuActionCmd2[menuActionCount] = option2;
            menuActionCmd3[menuActionCount] = option3;
            menuActionCount++;
            return;
        }
        if (spellSelected == 1) {
            if ((spellUsableOn & 2) == 2) {
                menuActionName[menuActionCount] = spellTooltip + " @yel@" + s;
                menuActionOpcode[menuActionCount] = 413;
                menuActionCmd1[menuActionCount] = option1;
                menuActionCmd2[menuActionCount] = option2;
                menuActionCmd3[menuActionCount] = option3;
                menuActionCount++;
            }
        } else {
            if (npc.actions != null) {
                for (int l = 4; l >= 0; l--) {
                    if (npc.actions[l] != null && !npc.actions[l].equalsIgnoreCase("attack")) {
                        menuActionName[menuActionCount] = npc.actions[l] + " @yel@" + s;
                        if (l == 0) {
                            menuActionOpcode[menuActionCount] = 20;
                        }
                        if (l == 1) {
                            menuActionOpcode[menuActionCount] = 412;
                        }
                        if (l == 2) {
                            menuActionOpcode[menuActionCount] = 225;
                        }
                        if (l == 3) {
                            menuActionOpcode[menuActionCount] = 965;
                        }
                        if (l == 4) {
                            menuActionOpcode[menuActionCount] = 478;
                        }
                        menuActionCmd1[menuActionCount] = option1;
                        menuActionCmd2[menuActionCount] = option2;
                        menuActionCmd3[menuActionCount] = option3;
                        menuActionCount++;
                    }
                }
            }
            if (npc.actions != null) {
                for (int i1 = 4; i1 >= 0; i1--) {
                    if (npc.actions[i1] != null && npc.actions[i1].equalsIgnoreCase("attack")) {
                        char c = '\0';
                        if (npc.combatLevel > Client.myPlayer.combatLevel) {
                            c = '\u07D0';
                        }
                        menuActionName[menuActionCount] = npc.actions[i1] + " @yel@" + s;
                        if (i1 == 0) {
                            menuActionOpcode[menuActionCount] = 20 + c;
                        }
                        if (i1 == 1) {
                            menuActionOpcode[menuActionCount] = 412 + c;
                        }
                        if (i1 == 2) {
                            menuActionOpcode[menuActionCount] = 225 + c;
                        }
                        if (i1 == 3) {
                            menuActionOpcode[menuActionCount] = 965 + c;
                        }
                        if (i1 == 4) {
                            menuActionOpcode[menuActionCount] = 478 + c;
                        }
                        menuActionCmd1[menuActionCount] = option1;
                        menuActionCmd2[menuActionCount] = option2;
                        menuActionCmd3[menuActionCount] = option3;
                        menuActionCount++;
                    }
                }
            }
            menuActionName[menuActionCount] = "Examine @yel@";
            menuActionOpcode[menuActionCount] = 1025;
            menuActionCmd1[menuActionCount] = option1;
            menuActionCmd2[menuActionCount] = option2;
            menuActionCmd3[menuActionCount] = option3;
            menuActionCount++;
        }
    }

    private void buildAtPlayerMenu(int option2, int option1, Player player, int option3) {
        if (player == Client.myPlayer) {
            return;
        }
        if (menuActionCount >= 400) {
            return;
        }
        String s;
        if (player.skillLevel == 0) {
            s = player.name + Client.getCombatLevelColor(Client.myPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + ")";
        } else {
            s = player.name + " (skill-" + player.skillLevel + ")";
        }
        if (itemSelected == 1) {
            menuActionName[menuActionCount] = "Use " + selectedItemName + " with @whi@" + s;
            menuActionOpcode[menuActionCount] = 491;
            menuActionCmd1[menuActionCount] = option1;
            menuActionCmd2[menuActionCount] = option2;
            menuActionCmd3[menuActionCount] = option3;
            menuActionCount++;
        } else if (spellSelected == 1) {
            if ((spellUsableOn & 8) == 8) {
                menuActionName[menuActionCount] = spellTooltip + " @whi@" + s;
                menuActionOpcode[menuActionCount] = 365;
                menuActionCmd1[menuActionCount] = option1;
                menuActionCmd2[menuActionCount] = option2;
                menuActionCmd3[menuActionCount] = option3;
                menuActionCount++;
            }
        } else {
            for (int l = 4; l >= 0; l--) {
                if (atPlayerActions[l] != null) {
                    menuActionName[menuActionCount] = atPlayerActions[l] + " @whi@" + s;
                    char c = '\0';
                    if (atPlayerActions[l].equalsIgnoreCase("attack")) {
                        if (player.combatLevel > Client.myPlayer.combatLevel) {
                            c = '\u07D0';
                        }
                        if (Client.myPlayer.team != 0 && player.team != 0) {
                            if (Client.myPlayer.team == player.team) {
                                c = '\u07D0';
                            } else {
                                c = '\0';
                            }
                        }
                    } else if (atPlayerArray[l]) {
                        c = '\u07D0';
                    }
                    if (l == 0) {
                        menuActionOpcode[menuActionCount] = 561 + c;
                    }
                    if (l == 1) {
                        menuActionOpcode[menuActionCount] = 779 + c;
                    }
                    if (l == 2) {
                        menuActionOpcode[menuActionCount] = 27 + c;
                    }
                    if (l == 3) {
                        menuActionOpcode[menuActionCount] = 577 + c;
                    }
                    if (l == 4) {
                        menuActionOpcode[menuActionCount] = 729 + c;
                    }
                    menuActionCmd1[menuActionCount] = option1;
                    menuActionCmd2[menuActionCount] = option2;
                    menuActionCmd3[menuActionCount] = option3;
                    menuActionCount++;
                }
            }
        }
        for (int i1 = 0; i1 < menuActionCount; i1++) {
            if (menuActionOpcode[i1] == 516) {
                menuActionName[i1] = "Walk here @whi@" + s;
                return;
            }
        }
    }

    private void method89(SceneObject object) {
        int i = 0;
        int j = -1;
        int k = 0;
        int l = 0;
        if (object.anInt1296 == 0) {
            i = sceneGraph.getWallObjectUid(object.anInt1295, object.anInt1297, object.anInt1298);
        }
        if (object.anInt1296 == 1) {
            i = sceneGraph.getWallDecorationUid(object.anInt1295, object.anInt1297, object.anInt1298);
        }
        if (object.anInt1296 == 2) {
            i = sceneGraph.getInteractiveObjectUid(object.anInt1295, object.anInt1297, object.anInt1298);
        }
        if (object.anInt1296 == 3) {
            i = sceneGraph.getGroundDecorationUid(object.anInt1295, object.anInt1297, object.anInt1298);
        }
        if (i != 0) {
            int i1 = sceneGraph.method304(object.anInt1295, object.anInt1297, object.anInt1298, i);
            j = i >> 14 & 0x7fff;
            k = i1 & 0x1f;
            l = i1 >> 6;
        }
        object.anInt1299 = j;
        object.anInt1301 = k;
        object.anInt1300 = l;
    }

    private void method90() {
        for (int i = 0; i < anInt1062; i++) {
            if (anIntArray1250[i] <= 0) {
                boolean noWaveReplay = false;
                try {
                    if (anIntArray1207[i] == anInt874 && anIntArray1241[i] == anInt1289) {
                        if (!replayWave()) {
                            noWaveReplay = true;
                        }
                    } else {
                        Stream stream = Sound.method241(anIntArray1241[i], anIntArray1207[i]);
                        if (System.currentTimeMillis() + (long) (stream.offset / 22) > aLong1172 + (long) (anInt1257 / 22)) {
                            anInt1257 = stream.offset;
                            aLong1172 = System.currentTimeMillis();
                            if (saveWave(stream.payload, stream.offset)) {
                                anInt874 = anIntArray1207[i];
                                anInt1289 = anIntArray1241[i];
                            } else {
                                noWaveReplay = true;
                            }
                        }
                    }
                } catch (Exception exception) {
                }
                if (!noWaveReplay || anIntArray1250[i] == -5) {
                    anInt1062--;
                    for (int j = i; j < anInt1062; j++) {
                        anIntArray1207[j] = anIntArray1207[j + 1];
                        anIntArray1241[j] = anIntArray1241[j + 1];
                        anIntArray1250[j] = anIntArray1250[j + 1];
                    }
                    i--;
                } else {
                    anIntArray1250[i] = -5;
                }
            } else {
                anIntArray1250[i]--;
            }
        }
        if (prevSong > 0) {
            prevSong -= 20;
            if (prevSong < 0) {
                prevSong = 0;
            }
            if (prevSong == 0 && musicEnabled && !Client.lowMem) {
                nextSong = currentSong;
                songChanging = true;
                onDemandFetcher.method558(2, nextSong);
            }
        }
    }

    void startUp() {
        drawLoadingText(20, "Starting up");
        if (Signlink.sunjava) {
            super.minDelay = 5;
        }
        if (Client.aBoolean993) {
            // rsAlreadyLoaded = true;
            // return;
        }
        Client.aBoolean993 = true;
        boolean flag = true;
        String s = getHost();
        if (s.endsWith("jagex.com")) {
            flag = true;
        }
        if (s.endsWith("runescape.com")) {
            flag = true;
        }
        if (s.endsWith("192.168.1.2")) {
            flag = true;
        }
        if (s.endsWith("192.168.1.229")) {
            flag = true;
        }
        if (s.endsWith("192.168.1.228")) {
            flag = true;
        }
        if (s.endsWith("192.168.1.227")) {
            flag = true;
        }
        if (s.endsWith("192.168.1.226")) {
            flag = true;
        }
        if (s.endsWith("127.0.0.1")) {
            flag = true;
        }
        if (!flag) {
            genericLoadingError = true;
            return;
        }
        if (Signlink.cache_dat != null) {
            for (int i = 0; i < 5; i++) {
                cacheIndices[i] = new CacheFile(Signlink.cache_dat, Signlink.cache_idx[i], i + 1);
            }
        }
        try {
            // downloadCrcs();
            titleArchive = archiveForName(1, "title screen", "title", expectedCRCs[1], 25);
            smallFont = new Font(false, "p11_full", titleArchive);
            aFont_1271 = new Font(false, "p12_full", titleArchive);
            boldFont = new Font(false, "b12_full", titleArchive);
            Font aFont_1273 = new Font(true, "q8_full", titleArchive);
            drawTitleScreen();
            loadTitleScreen();
            Archive configArchive = archiveForName(2, "config", "config", expectedCRCs[2], 30);
            Archive interfaceArchive = archiveForName(3, "interface", "interface", expectedCRCs[3], 35);
            Archive mediaArchive = archiveForName(4, "2d graphics", "media", expectedCRCs[4], 40);
            Archive textureArchive = archiveForName(6, "textures", "textures", expectedCRCs[6], 45);
            Archive chatArchive = archiveForName(7, "chat system", "wordenc", expectedCRCs[7], 50);
            Archive soundArchive = archiveForName(8, "sound effects", "sounds", expectedCRCs[8], 55);
            byteGroundArray = new byte[4][104][104];
            intGroundArray = new int[4][105][105];
            sceneGraph = new SceneGraph(intGroundArray);
            for (int i = 0; i < 4; i++) {
                collisionMaps[i] = new CollisionMap();
            }
            minimap = new Sprite(512, 512);
            Archive archive_6 = archiveForName(5, "update list", "versionlist", expectedCRCs[5], 60);
            drawLoadingText(60, "Connecting to update server");
            onDemandFetcher = new OnDemandFetcher();
            onDemandFetcher.start(archive_6, this);
            Animation.unpackConfig(onDemandFetcher.getAnimCount());
            Model.allocate(onDemandFetcher.getFileCount(0), onDemandFetcher);
            if (!Client.lowMem) {
                nextSong = 0;
                try {
                    nextSong = Integer.parseInt(getParameter("music"));
                } catch (Exception exception) {
                }
                songChanging = true;
                onDemandFetcher.method558(2, nextSong);
                while (onDemandFetcher.getRemaining() > 0) {
                    processOnDemandQueue();
                    try {
                        Thread.sleep(100L);
                    } catch (Exception exception) {
                    }
                    if (onDemandFetcher.anInt1349 > 3) {
                        throwLoadError();
                        return;
                    }
                }
            }
            drawLoadingText(65, "Requesting animations");
            int len = onDemandFetcher.getFileCount(1);
            for (int i1 = 0; i1 < len; i1++) {
                onDemandFetcher.method558(1, i1);
            }
            while (onDemandFetcher.getRemaining() > 0) {
                int off = len - onDemandFetcher.getRemaining();
                if (off > 0) {
                    drawLoadingText(65, "Loading animations - " + off * 100 / len + "%");
                }
                processOnDemandQueue();
                try {
                    Thread.sleep(100L);
                } catch (Exception exception) {
                }
                if (onDemandFetcher.anInt1349 > 3) {
                    throwLoadError();
                    return;
                }
            }
            drawLoadingText(70, "Requesting models");
            len = onDemandFetcher.getFileCount(0);
            for (int k1 = 0; k1 < len; k1++) {
                int l1 = onDemandFetcher.getModelIndex(k1);
                if ((l1 & 1) != 0) {
                    onDemandFetcher.method558(0, k1);
                }
            }
            len = onDemandFetcher.getRemaining();
            while (onDemandFetcher.getRemaining() > 0) {
                int i2 = len - onDemandFetcher.getRemaining();
                if (i2 > 0) {
                    drawLoadingText(70, "Loading models - " + i2 * 100 / len + "%");
                }
                processOnDemandQueue();
                try {
                    Thread.sleep(100L);
                } catch (Exception exception) {
                }
            }
            if (cacheIndices[0] != null) {
                drawLoadingText(75, "Requesting maps");
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(0, 48, 47));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(1, 48, 47));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(0, 48, 48));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(1, 48, 48));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(0, 48, 49));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(1, 48, 49));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(0, 47, 47));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(1, 47, 47));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(0, 47, 48));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(1, 47, 48));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(0, 148, 48));
                onDemandFetcher.method558(3, onDemandFetcher.getMapCount(1, 148, 48));
                len = onDemandFetcher.getRemaining();
                while (onDemandFetcher.getRemaining() > 0) {
                    int off = len - onDemandFetcher.getRemaining();
                    if (off > 0) {
                        drawLoadingText(75, "Loading maps - " + off * 100 / len + "%");
                    }
                    processOnDemandQueue();
                    try {
                        Thread.sleep(100L);
                    } catch (Exception exception) {
                    }
                }
            }
            len = onDemandFetcher.getFileCount(0);
            for (int k2 = 0; k2 < len; k2++) {
                int modelCount = onDemandFetcher.getModelIndex(k2);
                byte b = 0;
                if ((modelCount & 8) != 0) {
                    b = 10;
                } else if ((modelCount & 0x20) != 0) {
                    b = 9;
                } else if ((modelCount & 0x10) != 0) {
                    b = 8;
                } else if ((modelCount & 0x40) != 0) {
                    b = 7;
                } else if ((modelCount & 0x80) != 0) {
                    b = 6;
                } else if ((modelCount & 2) != 0) {
                    b = 5;
                } else if ((modelCount & 4) != 0) {
                    b = 4;
                }
                if ((modelCount & 1) != 0) {
                    b = 3;
                }
                if (b != 0) {
                    onDemandFetcher.method563(b, 0, k2);
                }
            }
            onDemandFetcher.method554(Client.isMembers);
            if (!Client.lowMem) {
                int fileLen = onDemandFetcher.getFileCount(2);
                for (int i3 = 1; i3 < fileLen; i3++) {
                    if (onDemandFetcher.getMidiIndex(i3)) {
                        onDemandFetcher.method563((byte) 1, 2, i3);
                    }
                }
            }
            drawLoadingText(80, "Unpacking media");
            invBack = new IndexedSprite(mediaArchive, "invback", 0);
            chatBack = new IndexedSprite(mediaArchive, "chatback", 0);
            mapBack = new IndexedSprite(mediaArchive, "mapback", 0);
            backBase1 = new IndexedSprite(mediaArchive, "backbase1", 0);
            backBase2 = new IndexedSprite(mediaArchive, "backbase2", 0);
            backHmid1 = new IndexedSprite(mediaArchive, "backhmid1", 0);
            for (int i = 0; i < 13; i++) {
                sideIcons[i] = new IndexedSprite(mediaArchive, "sideicons", i);
            }
            compass = new Sprite(mediaArchive, "compass", 0);
            mapEdge = new Sprite(mediaArchive, "mapedge", 0);
            mapEdge.resize();
            try {
                for (int i = 0; i < 100; i++) {
                    mapScenes[i] = new IndexedSprite(mediaArchive, "mapscene", i);
                }
            } catch (Exception exception) {
            }
            try {
                for (int i = 0; i < 100; i++) {
                    mapFunctions[i] = new Sprite(mediaArchive, "mapfunction", i);
                }
            } catch (Exception exception) {
            }
            try {
                for (int i = 0; i < 20; i++) {
                    hitMarks[i] = new Sprite(mediaArchive, "hitmarks", i);
                }
            } catch (Exception exception) {
            }
            try {
                for (int i = 0; i < 6; i++) {
                    skullIcon[i] = new Sprite(mediaArchive, "headicons_pk", i);
                }
            } catch (Exception exception) {
            }
            try {
                for (int i = 0; i < 9; i++) {
                    prayerIcon[i] = new Sprite(mediaArchive, "headicons_prayer", i);
                }
            } catch (Exception exception) {
            }
            try {
                for (int i = 0; i < 6; i++) {
                    hintIcon[i] = new Sprite(mediaArchive, "headicons_hint", i);
                }
            } catch (Exception exception) {
            }
            mapFlag = new Sprite(mediaArchive, "mapmarker", 0);
            mapMarker = new Sprite(mediaArchive, "mapmarker", 1);
            for (int i = 0; i < 8; i++) {
                crosses[i] = new Sprite(mediaArchive, "cross", i);
            }
            mapDotItem = new Sprite(mediaArchive, "mapdots", 0);
            mapDotNPC = new Sprite(mediaArchive, "mapdots", 1);
            mapDotPlayer = new Sprite(mediaArchive, "mapdots", 2);
            mapDotFriend = new Sprite(mediaArchive, "mapdots", 3);
            mapDotTeam = new Sprite(mediaArchive, "mapdots", 4);
            scrollBar1 = new IndexedSprite(mediaArchive, "scrollbar", 0);
            scrollBar2 = new IndexedSprite(mediaArchive, "scrollbar", 1);
            redStone1 = new IndexedSprite(mediaArchive, "redstone1", 0);
            redStone2 = new IndexedSprite(mediaArchive, "redstone2", 0);
            redStone3 = new IndexedSprite(mediaArchive, "redstone3", 0);
            redStone1_2 = new IndexedSprite(mediaArchive, "redstone1", 0);
            redStone1_2.flipHorizontal();
            redStone2_2 = new IndexedSprite(mediaArchive, "redstone2", 0);
            redStone2_2.flipHorizontal();
            redStone1_3 = new IndexedSprite(mediaArchive, "redstone1", 0);
            redStone1_3.flipVertical();
            redStone2_3 = new IndexedSprite(mediaArchive, "redstone2", 0);
            redStone2_3.flipVertical();
            redStone3_2 = new IndexedSprite(mediaArchive, "redstone3", 0);
            redStone3_2.flipVertical();
            redStone1_4 = new IndexedSprite(mediaArchive, "redstone1", 0);
            redStone1_4.flipHorizontal();
            redStone1_4.flipVertical();
            redStone2_4 = new IndexedSprite(mediaArchive, "redstone2", 0);
            redStone2_4.flipHorizontal();
            redStone2_4.flipVertical();
            for (int i = 0; i < 2; i++) {
                modIcons[i] = new IndexedSprite(mediaArchive, "mod_icons", i);
            }
            Sprite sprite = new Sprite(mediaArchive, "backleft1", 0);
            backLeftIP1 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            sprite = new Sprite(mediaArchive, "backleft2", 0);
            backLeftIP2 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            sprite = new Sprite(mediaArchive, "backright1", 0);
            backRightIP1 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            sprite = new Sprite(mediaArchive, "backright2", 0);
            backRightIP2 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            sprite = new Sprite(mediaArchive, "backtop1", 0);
            backTopIP1 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            sprite = new Sprite(mediaArchive, "backvmid1", 0);
            backVmidIP1 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            sprite = new Sprite(mediaArchive, "backvmid2", 0);
            backVmidIP2 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            sprite = new Sprite(mediaArchive, "backvmid3", 0);
            backVmidIP3 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            sprite = new Sprite(mediaArchive, "backhmid2", 0);
            backVmidIP2_2 = new GraphicsBuffer(sprite.width, sprite.height, getGameComponent());
            sprite.drawFlippedSprite(0, 0);
            int i5 = (int) (Math.random() * 21D) - 10;
            int j5 = (int) (Math.random() * 21D) - 10;
            int k5 = (int) (Math.random() * 21D) - 10;
            int l5 = (int) (Math.random() * 41D) - 20;
            for (int i = 0; i < 100; i++) {
                if (mapFunctions[i] != null) {
                    mapFunctions[i].adjustColors(i5 + l5, j5 + l5, k5 + l5);
                }
                if (mapScenes[i] != null) {
                    mapScenes[i].adjustColors(i5 + l5, j5 + l5, k5 + l5);
                }
            }
            drawLoadingText(83, "Unpacking textures");
            Rasterizer.unpackConfig(textureArchive);
            Rasterizer.generatePalette(0.80000000000000004D);
            Rasterizer.resetTextures();
            drawLoadingText(86, "Unpacking config");
            Sequence.unpackConfig(configArchive);
            ObjectDefinition.unpackConfig(configArchive);
            Floor.unpackConfig(configArchive);
            ItemDefinition.unpackConfig(configArchive);
            NpcDefinition.unpackConfig(configArchive);
            IdentityKit.unpackConfig(configArchive);
            SpotAnimation.unpackConfig(configArchive);
            Varp.unpackConfig(configArchive);
            VarBit.unpackConfig(configArchive);
            ItemDefinition.isMembers = Client.isMembers;
            if (!Client.lowMem) {
                drawLoadingText(90, "Unpacking sounds");
                byte abyte0[] = soundArchive.get("sounds.dat");
                Stream stream = new Stream(abyte0);
                Sound.unpack(stream);
            }
            drawLoadingText(95, "Unpacking interfaces");
            Font fonts[] = {smallFont, aFont_1271, boldFont, aFont_1273};
            Interface.unpack(interfaceArchive, fonts, mediaArchive);
            drawLoadingText(100, "Preparing game engine");
            for (int j6 = 0; j6 < 33; j6++) {
                int k6 = 999;
                int i7 = 0;
                for (int k7 = 0; k7 < 34; k7++) {
                    if (mapBack.pixels[k7 + j6 * mapBack.width] == 0) {
                        if (k6 == 999) {
                            k6 = k7;
                        }
                        continue;
                    }
                    if (k6 == 999) {
                        continue;
                    }
                    i7 = k7;
                    break;
                }
                anIntArray968[j6] = k6;
                anIntArray1057[j6] = i7 - k6;
            }
            for (int l6 = 5; l6 < 156; l6++) {
                int j7 = 999;
                int l7 = 0;
                for (int j8 = 25; j8 < 172; j8++) {
                    if (mapBack.pixels[j8 + l6 * mapBack.width] == 0 && (j8 > 34 || l6 > 34)) {
                        if (j7 == 999) {
                            j7 = j8;
                        }
                        continue;
                    }
                    if (j7 == 999) {
                        continue;
                    }
                    l7 = j8;
                    break;
                }
                anIntArray1052[l6 - 5] = j7 - 25;
                anIntArray1229[l6 - 5] = l7 - j7;
            }
            Rasterizer.init3dCanvas(479, 96);
            anIntArray1180 = Rasterizer.lineOffsets;
            Rasterizer.init3dCanvas(190, 261);
            anIntArray1181 = Rasterizer.lineOffsets;
            Rasterizer.init3dCanvas(512, 334);
            anIntArray1182 = Rasterizer.lineOffsets;
            int ai[] = new int[9];
            for (int i8 = 0; i8 < 9; i8++) {
                int k8 = 128 + i8 * 32 + 15;
                int l8 = 600 + k8 * 3;
                int i9 = Rasterizer.sineTable[k8];
                ai[i8] = l8 * i9 >> 16;
            }
            SceneGraph.method310(500, 800, 512, 334, ai);
            Censor.loadConfig(chatArchive);
            mouseRecorder = new MouseRecorder(this);
            startRunnable(mouseRecorder, 10);
            AnimatedObject.client = this;
            ObjectDefinition.client = this;
            NpcDefinition.client = this;
            return;
        } catch (Exception exception) {
            Signlink.reporterror("loaderror " + aString1049 + " " + anInt1079);
        }
        loadingError = true;
    }

    private void updatePlayerList(Stream stream, int len) {
        while (stream.bitOffset + 10 < len * 8) {
            int j = stream.getBits(11);
            if (j == 2047) {
                break;
            }
            if (localPlayers[j] == null) {
                localPlayers[j] = new Player();
                if (playerAppearanceBuffers[j] != null) {
                    localPlayers[j].updatePlayer(playerAppearanceBuffers[j]);
                }
            }
            playerIndices[localPlayerCount++] = j;
            Player player = localPlayers[j];
            player.lastUpdate = Client.loopCycle;
            int k = stream.getBits(1);
            if (k == 1) {
                playerUpdateIndices[entityUpdateCount++] = j;
            }
            int l = stream.getBits(1);
            int i1 = stream.getBits(5);
            if (i1 > 15) {
                i1 -= 32;
            }
            int j1 = stream.getBits(5);
            if (j1 > 15) {
                j1 -= 32;
            }
            player.updatePosition(Client.myPlayer.walkQueueX[0] + j1, Client.myPlayer.walkQueueY[0] + i1, l == 1);
        }
        stream.endBitBlock();
    }

    private void processMainScreenClick() {
        if (anInt1021 != 0) {
            return;
        }
        if (super.clickMode3 == 1) {
            int x = super.saveClickX - 25 - 550;
            int y = super.saveClickY - 5 - 4;
            if (x >= 0 && y >= 0 && x < 146 && y < 151) {
                x -= 73;
                y -= 75;
                int k = cameraX + minimapInt2 & 0x7ff;
                int i1 = Rasterizer.sineTable[k];
                int j1 = Rasterizer.cosineTable[k];
                i1 = i1 * (minimapInt3 + 256) >> 8;
                j1 = j1 * (minimapInt3 + 256) >> 8;
                int k1 = y * i1 + x * j1 >> 11;
                int l1 = y * j1 - x * i1 >> 11;
                int i2 = Client.myPlayer.x + k1 >> 7;
                int j2 = Client.myPlayer.y - l1 >> 7;
                boolean flag1 = doWalkTo(1, 0, 0, 0, Client.myPlayer.walkQueueY[0], 0, 0, j2, Client.myPlayer.walkQueueX[0], true, i2);
                if (flag1) {
                    outputStream.writeByte(x);
                    outputStream.writeByte(y);
                    outputStream.writeShort(cameraX);
                    outputStream.writeByte(57);
                    outputStream.writeByte(minimapInt2);
                    outputStream.writeByte(minimapInt3);
                    outputStream.writeByte(89);
                    outputStream.writeShort(Client.myPlayer.x);
                    outputStream.writeShort(Client.myPlayer.y);
                    outputStream.writeByte(anInt1264);
                    outputStream.writeByte(63);
                }
            }
            Client.anInt1117++;
            if (Client.anInt1117 > 1151) {
                Client.anInt1117 = 0;
                outputStream.writeOpcode(246);
                outputStream.writeByte(0);
                int l = outputStream.offset;
                if ((int) (Math.random() * 2D) == 0) {
                    outputStream.writeByte(101);
                }
                outputStream.writeByte(197);
                outputStream.writeShort((int) (Math.random() * 65536D));
                outputStream.writeByte((int) (Math.random() * 256D));
                outputStream.writeByte(67);
                outputStream.writeShort(14214);
                if ((int) (Math.random() * 2D) == 0) {
                    outputStream.writeShort(29487);
                }
                outputStream.writeShort((int) (Math.random() * 65536D));
                if ((int) (Math.random() * 2D) == 0) {
                    outputStream.writeByte(220);
                }
                outputStream.writeByte(180);
                outputStream.writeSizeByte(outputStream.offset - l);
            }
        }
    }

    private String getStringValue(int value) { // interfaceIntToString
        if (value <= Integer.MAX_VALUE) {
            return String.valueOf(value);
        } else {
            return "*";
        }
    }

    private void showErrorScreen() {
        Graphics g = getGameComponent().getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, 765, 503);
        method4(1);
        if (loadingError) {
            aBoolean831 = false;
            g.setFont(new java.awt.Font("Helvetica", 1, 16));
            g.setColor(Color.yellow);
            int k = 35;
            g.drawString("Sorry, an error has occured whilst loading RuneScape", 30, k);
            k += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, k);
            k += 50;
            g.setColor(Color.white);
            g.setFont(new java.awt.Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, k);
            k += 30;
            g.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, k);
            k += 30;
            g.drawString("3: Try using a different game-world", 30, k);
            k += 30;
            g.drawString("4: Try rebooting your computer", 30, k);
            k += 30;
            g.drawString("5: Try selecting a different version of Java from the play-game menu", 30, k);
        }
        if (genericLoadingError) {
            aBoolean831 = false;
            g.setFont(new java.awt.Font("Helvetica", 1, 20));
            g.setColor(Color.white);
            g.drawString("Error - unable to load game!", 50, 50);
            g.drawString("To play RuneScape make sure you play from", 50, 100);
            g.drawString("http://www.runescape.com", 50, 150);
        }
        if (alreadyLoaded) {
            aBoolean831 = false;
            g.setColor(Color.yellow);
            int l = 35;
            g.drawString("Error a copy of RuneScape already appears to be loaded", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.setFont(new java.awt.Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, l);
            l += 30;
            g.drawString("2: Try rebooting your computer, and reloading", 30, l);
            l += 30;
        }
    }

    public URL getCodeBase() {
        if (Signlink.mainapp != null) {
            return Signlink.mainapp.getCodeBase();
        }
        try {
            if (super.frame != null) {
                return new URL("http://127.0.0.1:" + (80 + Client.portOff));
                // return new URL("http://ss-pk.no-ip.biz:" + 43594);
            }
        } catch (Exception exception) {
        }
        return super.getCodeBase();
    }

    private void method95() {
        for (int i = 0; i < localNpcCount; i++) {
            int k = localNpcIndices[i];
            Npc npc = localNpcs[k];
            if (npc != null) {
                method96(npc);
            }
        }
    }

    private void method96(Mobile mobile) {
        if (mobile.x < 128 || mobile.y < 128 || mobile.x >= 13184 || mobile.y >= 13184) {
            mobile.animationId = -1;
            mobile.currentGraphic = -1;
            mobile.anInt1547 = 0;
            mobile.anInt1548 = 0;
            mobile.x = mobile.walkQueueX[0] * 128 + mobile.anInt1540 * 64;
            mobile.y = mobile.walkQueueY[0] * 128 + mobile.anInt1540 * 64;
            mobile.method446();
        }
        if (mobile == Client.myPlayer && (mobile.x < 1536 || mobile.y < 1536 || mobile.x >= 11776 || mobile.y >= 11776)) {
            mobile.animationId = -1;
            mobile.currentGraphic = -1;
            mobile.anInt1547 = 0;
            mobile.anInt1548 = 0;
            mobile.x = mobile.walkQueueX[0] * 128 + mobile.anInt1540 * 64;
            mobile.y = mobile.walkQueueY[0] * 128 + mobile.anInt1540 * 64;
            mobile.method446();
        }
        if (mobile.anInt1547 > Client.loopCycle) {
            method97(mobile);
        } else if (mobile.anInt1548 >= Client.loopCycle) {
            method98(mobile);
        } else {
            method99(mobile);
        }
        method100(mobile);
        method101(mobile);
    }

    private void method97(Mobile mobile) {
        int i = mobile.anInt1547 - Client.loopCycle;
        int j = mobile.anInt1543 * 128 + mobile.anInt1540 * 64;
        int k = mobile.anInt1545 * 128 + mobile.anInt1540 * 64;
        mobile.x += (j - mobile.x) / i;
        mobile.y += (k - mobile.y) / i;
        mobile.anInt1503 = 0;
        if (mobile.anInt1549 == 0) {
            mobile.turnDirection = 1024;
        }
        if (mobile.anInt1549 == 1) {
            mobile.turnDirection = 1536;
        }
        if (mobile.anInt1549 == 2) {
            mobile.turnDirection = 0;
        }
        if (mobile.anInt1549 == 3) {
            mobile.turnDirection = 512;
        }
    }

    private void method98(Mobile mobile) {
        if (mobile.anInt1548 == Client.loopCycle || mobile.animationId == -1 || mobile.animationDelay != 0 || mobile.anInt1528 + 1 > Sequence.sequenceCache[mobile.animationId].getFrameLength(mobile.anInt1527)) {
            int i = mobile.anInt1548 - mobile.anInt1547;
            int j = Client.loopCycle - mobile.anInt1547;
            int k = mobile.anInt1543 * 128 + mobile.anInt1540 * 64;
            int l = mobile.anInt1545 * 128 + mobile.anInt1540 * 64;
            int i1 = mobile.anInt1544 * 128 + mobile.anInt1540 * 64;
            int j1 = mobile.anInt1546 * 128 + mobile.anInt1540 * 64;
            mobile.x = (k * (i - j) + i1 * j) / i;
            mobile.y = (l * (i - j) + j1 * j) / i;
        }
        mobile.anInt1503 = 0;
        if (mobile.anInt1549 == 0) {
            mobile.turnDirection = 1024;
        }
        if (mobile.anInt1549 == 1) {
            mobile.turnDirection = 1536;
        }
        if (mobile.anInt1549 == 2) {
            mobile.turnDirection = 0;
        }
        if (mobile.anInt1549 == 3) {
            mobile.turnDirection = 512;
        }
        mobile.anInt1552 = mobile.turnDirection;
    }

    private void method99(Mobile mobile) {
        mobile.anInt1517 = mobile.idleAnimation;
        if (mobile.walkQueueLocationIndex == 0) {
            mobile.anInt1503 = 0;
            return;
        }
        if (mobile.animationId != -1 && mobile.animationDelay == 0) {
            Sequence sequence = Sequence.sequenceCache[mobile.animationId];
            if (mobile.anInt1542 > 0 && sequence.anInt363 == 0) {
                mobile.anInt1503++;
                return;
            }
            if (mobile.anInt1542 <= 0 && sequence.walkProperties == 0) {
                mobile.anInt1503++;
                return;
            }
        }
        int i = mobile.x;
        int j = mobile.y;
        int k = mobile.walkQueueX[mobile.walkQueueLocationIndex - 1] * 128 + mobile.anInt1540 * 64;
        int l = mobile.walkQueueY[mobile.walkQueueLocationIndex - 1] * 128 + mobile.anInt1540 * 64;
        if (k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
            mobile.x = k;
            mobile.y = l;
            return;
        }
        if (i < k) {
            if (j < l) {
                mobile.turnDirection = 1280;
            } else if (j > l) {
                mobile.turnDirection = 1792;
            } else {
                mobile.turnDirection = 1536;
            }
        } else if (i > k) {
            if (j < l) {
                mobile.turnDirection = 768;
            } else if (j > l) {
                mobile.turnDirection = 256;
            } else {
                mobile.turnDirection = 512;
            }
        } else if (j < l) {
            mobile.turnDirection = 1024;
        } else {
            mobile.turnDirection = 0;
        }
        int i1 = mobile.turnDirection - mobile.anInt1552 & 0x7ff;
        if (i1 > 1024) {
            i1 -= 2048;
        }
        int j1 = mobile.anInt1555;
        if (i1 >= -256 && i1 <= 256) {
            j1 = mobile.walkAnimation;
        } else if (i1 >= 256 && i1 < 768) {
            j1 = mobile.anInt1557;
        } else if (i1 >= -768 && i1 <= -256) {
            j1 = mobile.anInt1556;
        }
        if (j1 == -1) {
            j1 = mobile.walkAnimation;
        }
        mobile.anInt1517 = j1;
        int k1 = 4;
        if (mobile.anInt1552 != mobile.turnDirection && mobile.interactingEntity == -1 && mobile.defaultTurnValue != 0) {
            k1 = 2;
        }
        if (mobile.walkQueueLocationIndex > 2) {
            k1 = 6;
        }
        if (mobile.walkQueueLocationIndex > 3) {
            k1 = 8;
        }
        if (mobile.anInt1503 > 0 && mobile.walkQueueLocationIndex > 1) {
            k1 = 8;
            mobile.anInt1503--;
        }
        if (mobile.runningFlags[mobile.walkQueueLocationIndex - 1]) {
            k1 <<= 1;
        }
        if (k1 >= 8 && mobile.anInt1517 == mobile.walkAnimation && mobile.runAnimation != -1) {
            mobile.anInt1517 = mobile.runAnimation;
        }
        if (i < k) {
            mobile.x += k1;
            if (mobile.x > k) {
                mobile.x = k;
            }
        } else if (i > k) {
            mobile.x -= k1;
            if (mobile.x < k) {
                mobile.x = k;
            }
        }
        if (j < l) {
            mobile.y += k1;
            if (mobile.y > l) {
                mobile.y = l;
            }
        } else if (j > l) {
            mobile.y -= k1;
            if (mobile.y < l) {
                mobile.y = l;
            }
        }
        if (mobile.x == k && mobile.y == l) {
            mobile.walkQueueLocationIndex--;
            if (mobile.anInt1542 > 0) {
                mobile.anInt1542--;
            }
        }
    }

    private void method100(Mobile mobile) {
        if (mobile.defaultTurnValue == 0) {
            return;
        }
        if (mobile.interactingEntity != -1 && mobile.interactingEntity < 32768) {
            Npc npc = localNpcs[mobile.interactingEntity];
            if (npc != null) {
                int i1 = mobile.x - npc.x;
                int k1 = mobile.y - npc.y;
                if (i1 != 0 || k1 != 0) {
                    mobile.turnDirection = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
                }
            }
        }
        if (mobile.interactingEntity >= 32768) {
            int j = mobile.interactingEntity - 32768;
            if (j == unknownInt10) {
                j = myPlayerIndex;
            }
            Player player = localPlayers[j];
            if (player != null) {
                int l1 = mobile.x - player.x;
                int i2 = mobile.y - player.y;
                if (l1 != 0 || i2 != 0) {
                    mobile.turnDirection = (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
                }
            }
        }
        if ((mobile.anInt1538 != 0 || mobile.anInt1539 != 0) && (mobile.walkQueueLocationIndex == 0 || mobile.anInt1503 > 0)) {
            int k = mobile.x - (mobile.anInt1538 - baseX - baseX) * 64;
            int j1 = mobile.y - (mobile.anInt1539 - baseY - baseY) * 64;
            if (k != 0 || j1 != 0) {
                mobile.turnDirection = (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
            }
            mobile.anInt1538 = 0;
            mobile.anInt1539 = 0;
        }
        int l = mobile.turnDirection - mobile.anInt1552 & 0x7ff;
        if (l != 0) {
            if (l < mobile.defaultTurnValue || l > 2048 - mobile.defaultTurnValue) {
                mobile.anInt1552 = mobile.turnDirection;
            } else if (l > 1024) {
                mobile.anInt1552 -= mobile.defaultTurnValue;
            } else {
                mobile.anInt1552 += mobile.defaultTurnValue;
            }
            mobile.anInt1552 &= 0x7ff;
            if (mobile.anInt1517 == mobile.idleAnimation && mobile.anInt1552 != mobile.turnDirection) {
                if (mobile.turnAnimation != -1) {
                    mobile.anInt1517 = mobile.turnAnimation;
                    return;
                }
                mobile.anInt1517 = mobile.walkAnimation;
            }
        }
    }

    private void method101(Mobile mobile) {
        mobile.aBoolean1541 = false;
        if (mobile.anInt1517 != -1) {
            Sequence sequence = Sequence.sequenceCache[mobile.anInt1517];
            mobile.anInt1519++;
            if (mobile.anInt1518 < sequence.length && mobile.anInt1519 > sequence.getFrameLength(mobile.anInt1518)) {
                mobile.anInt1519 = 0;
                mobile.anInt1518++;
            }
            if (mobile.anInt1518 >= sequence.length) {
                mobile.anInt1519 = 0;
                mobile.anInt1518 = 0;
            }
        }
        if (mobile.currentGraphic != -1 && Client.loopCycle >= mobile.anInt1523) {
            if (mobile.anInt1521 < 0) {
                mobile.anInt1521 = 0;
            }
            Sequence sequence_1 = SpotAnimation.spotAnimationCache[mobile.currentGraphic].sequence;
            for (mobile.anInt1522++; mobile.anInt1521 < sequence_1.length && mobile.anInt1522 > sequence_1.getFrameLength(mobile.anInt1521); mobile.anInt1521++) {
                mobile.anInt1522 -= sequence_1.getFrameLength(mobile.anInt1521);
            }
            if (mobile.anInt1521 >= sequence_1.length && (mobile.anInt1521 < 0 || mobile.anInt1521 >= sequence_1.length)) {
                mobile.currentGraphic = -1;
            }
        }
        if (mobile.animationId != -1 && mobile.animationDelay <= 1) {
            Sequence sequence_2 = Sequence.sequenceCache[mobile.animationId];
            if (sequence_2.anInt363 == 1 && mobile.anInt1542 > 0 && mobile.anInt1547 <= Client.loopCycle && mobile.anInt1548 < Client.loopCycle) {
                mobile.animationDelay = 1;
                return;
            }
        }
        if (mobile.animationId != -1 && mobile.animationDelay == 0) {
            Sequence sequence_3 = Sequence.sequenceCache[mobile.animationId];
            for (mobile.anInt1528++; mobile.anInt1527 < sequence_3.length && mobile.anInt1528 > sequence_3.getFrameLength(mobile.anInt1527); mobile.anInt1527++) {
                mobile.anInt1528 -= sequence_3.getFrameLength(mobile.anInt1527);
            }
            if (mobile.anInt1527 >= sequence_3.length) {
                mobile.anInt1527 -= sequence_3.frameStep;
                mobile.anInt1530++;
                if (mobile.anInt1530 >= sequence_3.anInt362) {
                    mobile.animationId = -1;
                }
                if (mobile.anInt1527 < 0 || mobile.anInt1527 >= sequence_3.length) {
                    mobile.animationId = -1;
                }
            }
            mobile.aBoolean1541 = sequence_3.aBoolean358;
        }
        if (mobile.animationDelay > 0) {
            mobile.animationDelay--;
        }
    }

    private void drawGameScreen() {
        if (welcomeScreenRaised) {
            welcomeScreenRaised = false;
            backLeftIP1.drawGraphics(4, super.graphics, 0);
            backLeftIP2.drawGraphics(357, super.graphics, 0);
            backRightIP1.drawGraphics(4, super.graphics, 722);
            backRightIP2.drawGraphics(205, super.graphics, 743);
            backTopIP1.drawGraphics(0, super.graphics, 0);
            backVmidIP1.drawGraphics(4, super.graphics, 516);
            backVmidIP2.drawGraphics(205, super.graphics, 516);
            backVmidIP3.drawGraphics(357, super.graphics, 496);
            backVmidIP2_2.drawGraphics(338, super.graphics, 0);
            needDrawTabArea = true;
            inputTaken = true;
            tabAreaAltered = true;
            aBoolean1233 = true;
            if (loadingStage != 2) {
                gameScreenCanvas.drawGraphics(4, super.graphics, 4);
                minimapCanvas.drawGraphics(4, super.graphics, 550);
            }
        }
        if (loadingStage == 2) {
            method146();
        }
        if (menuOpen && menuScreenArea == 1) {
            needDrawTabArea = true;
        }
        if (invOverlayInterfaceID != -1) {
            boolean flag1 = method119(anInt945, invOverlayInterfaceID);
            if (flag1) {
                needDrawTabArea = true;
            }
        }
        if (atInventoryInterfaceType == 2) {
            needDrawTabArea = true;
        }
        if (activeInterfaceType == 2) {
            needDrawTabArea = true;
        }
        if (needDrawTabArea) {
            drawTabArea();
            needDrawTabArea = false;
        }
        if (backDialogID == -1) {
            anInterface_1059.scrollPosition = anInt1211 - chatboxScrollerPos - 77;
            if (super.mouseX > 448 && super.mouseX < 560 && super.mouseY > 332) {
                method65(463, 77, super.mouseX - 17, super.mouseY - 357, anInterface_1059, 0, false, anInt1211);
            }
            int i = anInt1211 - 77 - anInterface_1059.scrollPosition;
            if (i < 0) {
                i = 0;
            }
            if (i > anInt1211 - 77) {
                i = anInt1211 - 77;
            }
            if (chatboxScrollerPos != i) {
                chatboxScrollerPos = i;
                inputTaken = true;
            }
        }
        if (backDialogID != -1) {
            boolean flag2 = method119(anInt945, backDialogID);
            if (flag2) {
                inputTaken = true;
            }
        }
        if (atInventoryInterfaceType == 3) {
            inputTaken = true;
        }
        if (activeInterfaceType == 3) {
            inputTaken = true;
        }
        if (aString844 != null) {
            inputTaken = true;
        }
        if (menuOpen && menuScreenArea == 2) {
            inputTaken = true;
        }
        if (inputTaken) {
            drawChatArea();
            inputTaken = false;
        }
        if (loadingStage == 2) {
            drawMinimap();
            minimapCanvas.drawGraphics(4, super.graphics, 550);
        }
        if (anInt1054 != -1) {
            tabAreaAltered = true;
        }
        if (tabAreaAltered) {
            if (anInt1054 != -1 && anInt1054 == tabID) {
                anInt1054 = -1;
                outputStream.writeOpcode(120);
                outputStream.writeByte(tabID);
            }
            tabAreaAltered = false;
            aGraphicsBuffer_1125.initDrawingArea();
            backHmid1.drawIndexedSprite(0, 0);
            if (invOverlayInterfaceID == -1) {
                if (tabInterfaceIDs[tabID] != -1) {
                    if (tabID == 0) {
                        redStone1.drawIndexedSprite(22, 10);
                    }
                    if (tabID == 1) {
                        redStone2.drawIndexedSprite(54, 8);
                    }
                    if (tabID == 2) {
                        redStone2.drawIndexedSprite(82, 8);
                    }
                    if (tabID == 3) {
                        redStone3.drawIndexedSprite(110, 8);
                    }
                    if (tabID == 4) {
                        redStone2_2.drawIndexedSprite(153, 8);
                    }
                    if (tabID == 5) {
                        redStone2_2.drawIndexedSprite(181, 8);
                    }
                    if (tabID == 6) {
                        redStone1_2.drawIndexedSprite(209, 9);
                    }
                }
                if (tabInterfaceIDs[0] != -1 && (anInt1054 != 0 || Client.loopCycle % 20 < 10)) {
                    sideIcons[0].drawIndexedSprite(29, 13);
                }
                if (tabInterfaceIDs[1] != -1 && (anInt1054 != 1 || Client.loopCycle % 20 < 10)) {
                    sideIcons[1].drawIndexedSprite(53, 11);
                }
                if (tabInterfaceIDs[2] != -1 && (anInt1054 != 2 || Client.loopCycle % 20 < 10)) {
                    sideIcons[2].drawIndexedSprite(82, 11);
                }
                if (tabInterfaceIDs[3] != -1 && (anInt1054 != 3 || Client.loopCycle % 20 < 10)) {
                    sideIcons[3].drawIndexedSprite(115, 12);
                }
                if (tabInterfaceIDs[4] != -1 && (anInt1054 != 4 || Client.loopCycle % 20 < 10)) {
                    sideIcons[4].drawIndexedSprite(153, 13);
                }
                if (tabInterfaceIDs[5] != -1 && (anInt1054 != 5 || Client.loopCycle % 20 < 10)) {
                    sideIcons[5].drawIndexedSprite(180, 11);
                }
                if (tabInterfaceIDs[6] != -1 && (anInt1054 != 6 || Client.loopCycle % 20 < 10)) {
                    sideIcons[6].drawIndexedSprite(208, 13);
                }
            }
            aGraphicsBuffer_1125.drawGraphics(160, super.graphics, 516);
            aGraphicsBuffer_1124.initDrawingArea();
            backBase2.drawIndexedSprite(0, 0);
            if (invOverlayInterfaceID == -1) {
                if (tabInterfaceIDs[tabID] != -1) {
                    if (tabID == 7) {
                        redStone1_3.drawIndexedSprite(42, 0);
                    }
                    if (tabID == 8) {
                        redStone2_3.drawIndexedSprite(74, 0);
                    }
                    if (tabID == 9) {
                        redStone2_3.drawIndexedSprite(102, 0);
                    }
                    if (tabID == 10) {
                        redStone3_2.drawIndexedSprite(130, 1);
                    }
                    if (tabID == 11) {
                        redStone2_4.drawIndexedSprite(173, 0);
                    }
                    if (tabID == 12) {
                        redStone2_4.drawIndexedSprite(201, 0);
                    }
                    if (tabID == 13) {
                        redStone1_4.drawIndexedSprite(229, 0);
                    }
                }
                if (tabInterfaceIDs[8] != -1 && (anInt1054 != 8 || Client.loopCycle % 20 < 10)) {
                    sideIcons[7].drawIndexedSprite(74, 2);
                }
                if (tabInterfaceIDs[9] != -1 && (anInt1054 != 9 || Client.loopCycle % 20 < 10)) {
                    sideIcons[8].drawIndexedSprite(102, 3);
                }
                if (tabInterfaceIDs[10] != -1 && (anInt1054 != 10 || Client.loopCycle % 20 < 10)) {
                    sideIcons[9].drawIndexedSprite(137, 4);
                }
                if (tabInterfaceIDs[11] != -1 && (anInt1054 != 11 || Client.loopCycle % 20 < 10)) {
                    sideIcons[10].drawIndexedSprite(174, 2);
                }
                if (tabInterfaceIDs[12] != -1 && (anInt1054 != 12 || Client.loopCycle % 20 < 10)) {
                    sideIcons[11].drawIndexedSprite(201, 2);
                }
                if (tabInterfaceIDs[13] != -1 && (anInt1054 != 13 || Client.loopCycle % 20 < 10)) {
                    sideIcons[12].drawIndexedSprite(226, 2);
                }
            }
            aGraphicsBuffer_1124.drawGraphics(466, super.graphics, 496);
            gameScreenCanvas.initDrawingArea();
        }
        if (aBoolean1233) {
            aBoolean1233 = false;
            aGraphicsBuffer_1123.initDrawingArea();
            backBase1.drawIndexedSprite(0, 0);
            aFont_1271.drawCenteredText(0xffffff, 55, "Public chat", 28, true);
            if (publicChatMode == 0) {
                aFont_1271.drawCenteredText(65280, 55, "On", 41, true);
            }
            if (publicChatMode == 1) {
                aFont_1271.drawCenteredText(0xffff00, 55, "Friends", 41, true);
            }
            if (publicChatMode == 2) {
                aFont_1271.drawCenteredText(0xff0000, 55, "Off", 41, true);
            }
            if (publicChatMode == 3) {
                aFont_1271.drawCenteredText(65535, 55, "Hide", 41, true);
            }
            aFont_1271.drawCenteredText(0xffffff, 184, "Private chat", 28, true);
            if (privateChatMode == 0) {
                aFont_1271.drawCenteredText(65280, 184, "On", 41, true);
            }
            if (privateChatMode == 1) {
                aFont_1271.drawCenteredText(0xffff00, 184, "Friends", 41, true);
            }
            if (privateChatMode == 2) {
                aFont_1271.drawCenteredText(0xff0000, 184, "Off", 41, true);
            }
            aFont_1271.drawCenteredText(0xffffff, 324, "Trade/compete", 28, true);
            if (tradeMode == 0) {
                aFont_1271.drawCenteredText(65280, 324, "On", 41, true);
            }
            if (tradeMode == 1) {
                aFont_1271.drawCenteredText(0xffff00, 324, "Friends", 41, true);
            }
            if (tradeMode == 2) {
                aFont_1271.drawCenteredText(0xff0000, 324, "Off", 41, true);
            }
            aFont_1271.drawCenteredText(0xffffff, 458, "Report abuse", 33, true);
            aGraphicsBuffer_1123.drawGraphics(453, super.graphics, 0);
            gameScreenCanvas.initDrawingArea();
        }
        anInt945 = 0;
    }

    private boolean buildFriendsListMenu(Interface component) {
        int i = component.contentType;
        if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
            if (i >= 801) {
                i -= 701;
            } else if (i >= 701) {
                i -= 601;
            } else if (i >= 101) {
                i -= 101;
            } else {
                i--;
            }
            menuActionName[menuActionCount] = "Remove @whi@" + friendsNames[i];
            menuActionOpcode[menuActionCount] = 792;
            menuActionCount++;
            menuActionName[menuActionCount] = "Message @whi@" + friendsNames[i];
            menuActionOpcode[menuActionCount] = 639;
            menuActionCount++;
            return true;
        }
        if (i >= 401 && i <= 500) {
            menuActionName[menuActionCount] = "Remove @whi@" + component.message;
            menuActionOpcode[menuActionCount] = 322;
            menuActionCount++;
            return true;
        } else {
            return false;
        }
    }

    private void renderStillGraphics() { // method104
        StillGraphic stillGraphic = (StillGraphic) stillGraphicList.getFront();
        for (; stillGraphic != null; stillGraphic = (StillGraphic) stillGraphicList.getNext()) {
            if (stillGraphic.anInt1560 != plane || stillGraphic.aBoolean1567) {
                stillGraphic.unlink();
            } else if (Client.loopCycle >= stillGraphic.anInt1564) {
                stillGraphic.method454(anInt945);
                if (stillGraphic.aBoolean1567) {
                    stillGraphic.unlink();
                } else {
                    sceneGraph.method285(stillGraphic.anInt1560, 0, stillGraphic.anInt1563, -1, stillGraphic.anInt1562, 60, stillGraphic.anInt1561, stillGraphic, false);
                }
            }
        }
    }

    private void drawInterface(int scrollPos, int x, Interface parent, int y) {
        if (parent.interfaceType != 0 || parent.children == null) {
            return;
        }
        if (parent.aBoolean266 && anInt1026 != parent.id && anInt1048 != parent.id && anInt1039 != parent.id) {
            return;
        }
        int i1 = Graphics2D.topX;
        int j1 = Graphics2D.topY;
        int k1 = Graphics2D.bottomX;
        int l1 = Graphics2D.bottomY;
        Graphics2D.setBounds(y + parent.height, x, x + parent.width, y);
        int i2 = parent.children.length;
        for (int j2 = 0; j2 < i2; j2++) {
            int k2 = parent.childX[j2] + x;
            int l2 = parent.childY[j2] + y - scrollPos;
            Interface child = Interface.cachedInterfaces[parent.children[j2]];
            k2 += child.anInt263;
            l2 += child.anInt265;
            if (child.contentType > 0) {
                drawLoginInterfaces(child);
            }
            if (child.interfaceType == 0) {
                if (child.scrollPosition > child.scrollMax - child.height) {
                    child.scrollPosition = child.scrollMax - child.height;
                }
                if (child.scrollPosition < 0) {
                    child.scrollPosition = 0;
                }
                drawInterface(child.scrollPosition, k2, child, l2);
                if (child.scrollMax > child.height) {
                    drawScroller(child.height, child.scrollPosition, l2, k2 + child.width, child.scrollMax);
                }
            } else if (child.interfaceType != 1) {
                if (child.interfaceType == 2) {
                    int i3 = 0;
                    for (int l3 = 0; l3 < child.height; l3++) {
                        for (int l4 = 0; l4 < child.width; l4++) {
                            int k5 = k2 + l4 * (32 + child.invSpritePadX);
                            int j6 = l2 + l3 * (32 + child.invSpritePadY);
                            if (i3 < 20) {
                                k5 += child.spritesX[i3];
                                j6 += child.spritesY[i3];
                            }
                            if (child.inv[i3] > 0) {
                                int k6 = 0;
                                int j7 = 0;
                                int j9 = child.inv[i3] - 1;
                                if (k5 > Graphics2D.topX - 32 && k5 < Graphics2D.bottomX && j6 > Graphics2D.topY - 32 && j6 < Graphics2D.bottomY || activeInterfaceType != 0 && anInt1085 == i3) {
                                    int l9 = 0;
                                    if (itemSelected == 1 && anInt1283 == i3 && anInt1284 == child.id) {
                                        l9 = 0xffffff;
                                    }
                                    Sprite class30_sub2_sub1_sub1_2 = ItemDefinition.getSprite(j9, child.invStackSizes[i3], l9);
                                    if (class30_sub2_sub1_sub1_2 != null) {
                                        if (activeInterfaceType != 0 && anInt1085 == i3 && anInt1084 == child.id) {
                                            k6 = super.mouseX - anInt1087;
                                            j7 = super.mouseY - anInt1088;
                                            if (k6 < 5 && k6 > -5) {
                                                k6 = 0;
                                            }
                                            if (j7 < 5 && j7 > -5) {
                                                j7 = 0;
                                            }
                                            if (anInt989 < 5) {
                                                k6 = 0;
                                                j7 = 0;
                                            }
                                            class30_sub2_sub1_sub1_2.drawSprite(k5 + k6, j6 + j7,
                                                    128);
                                            if (j6 + j7 < Graphics2D.topY && parent.scrollPosition > 0) {
                                                int i10 = anInt945 * (Graphics2D.topY - j6 - j7) / 3;
                                                if (i10 > anInt945 * 10) {
                                                    i10 = anInt945 * 10;
                                                }
                                                if (i10 > parent.scrollPosition) {
                                                    i10 = parent.scrollPosition;
                                                }
                                                parent.scrollPosition -= i10;
                                                anInt1088 += i10;
                                            }
                                            if (j6 + j7 + 32 > Graphics2D.bottomY && parent.scrollPosition < parent.scrollMax - parent.height) {
                                                int j10 = anInt945 * (j6 + j7 + 32 - Graphics2D.bottomY) / 3;
                                                if (j10 > anInt945 * 10) {
                                                    j10 = anInt945 * 10;
                                                }
                                                if (j10 > parent.scrollMax - parent.height - parent.scrollPosition) {
                                                    j10 = parent.scrollMax - parent.height - parent.scrollPosition;
                                                }
                                                parent.scrollPosition += j10;
                                                anInt1088 -= j10;
                                            }
                                        } else if (atInventoryInterfaceType != 0 && atInventoryIndex == i3 && atInventoryInterface == child.id) {
                                            class30_sub2_sub1_sub1_2.drawSprite(k5, j6, 128);
                                        } else {
                                            class30_sub2_sub1_sub1_2.drawSprite(k5, j6);
                                        }
                                        if (class30_sub2_sub1_sub1_2.trimWidth == 33 || child.invStackSizes[i3] != 1) {
                                            int k10 = child.invStackSizes[i3];
                                            smallFont.drawText(0, Client.getFormattedValue(k10), j6 + 10 + j7, k5 + 1 + k6);
                                            smallFont.drawText(0xffff00, Client.getFormattedValue(k10), j6 + 9 + j7, k5 + k6);
                                        }
                                    }
                                }
                            } else if (child.sprites != null && i3 < 20) {
                                Sprite class30_sub2_sub1_sub1_1 = child.sprites[i3];
                                if (class30_sub2_sub1_sub1_1 != null) {
                                    class30_sub2_sub1_sub1_1.drawSprite(k5, j6);
                                }
                            }
                            i3++;
                        }
                    }
                } else if (child.interfaceType == 3) {
                    boolean flag = false;
                    if (anInt1039 == child.id || anInt1048 == child.id || anInt1026 == child.id) {
                        flag = true;
                    }
                    int j3;
                    if (interfaceEnabled(child)) {
                        j3 = child.anInt219;
                        if (flag && child.anInt239 != 0) {
                            j3 = child.anInt239;
                        }
                    } else {
                        j3 = child.textColor;
                        if (flag && child.anInt216 != 0) {
                            j3 = child.anInt216;
                        }
                    }
                    if (child.alpha == 0) {
                        if (child.aBoolean227) {
                            Graphics2D.fillRect(child.height, l2, k2, j3, child.width);
                        } else {
                            Graphics2D.drawRect(k2, child.width, child.height, j3, l2);
                        }
                    } else if (child.aBoolean227) {
                        Graphics2D.fillRect(j3, l2, child.width, child.height, 256 - (child.alpha & 0xff), k2);
                    } else {
                        Graphics2D.drawRect(l2, child.height, 256 - (child.alpha & 0xff), j3, child.width, k2);
                    }
                } else if (child.interfaceType == 4) {
                    Font font = child.fonts;
                    String s = child.message;
                    boolean flag1 = false;
                    if (anInt1039 == child.id || anInt1048 == child.id || anInt1026 == child.id) {
                        flag1 = true;
                    }
                    int i4;
                    if (interfaceEnabled(child)) {
                        i4 = child.anInt219;
                        if (flag1 && child.anInt239 != 0) {
                            i4 = child.anInt239;
                        }
                        if (child.aString228.length() > 0) {
                            s = child.aString228;
                        }
                    } else {
                        i4 = child.textColor;
                        if (flag1 && child.anInt216 != 0) {
                            i4 = child.anInt216;
                        }
                    }
                    if (child.actionType == 6 && aBoolean1149) {
                        s = "Please wait...";
                        i4 = child.textColor;
                    }
                    if (Graphics2D.width == 479) {
                        if (i4 == 0xffff00) {
                            i4 = 255;
                        }
                        if (i4 == 49152) {
                            i4 = 0xffffff;
                        }
                    }
                    for (int l6 = l2 + font.trimHeight; s.length() > 0; l6 += font.trimHeight) {
                        if (s.indexOf("%") != -1) {
                            do {
                                int k7 = s.indexOf("%1");
                                if (k7 == -1) {
                                    break;
                                }
                                s = s.substring(0, k7) + getStringValue(extractInterfaceValues(child, 0)) + s.substring(k7 + 2);
                            } while (true);
                            do {
                                int l7 = s.indexOf("%2");
                                if (l7 == -1) {
                                    break;
                                }
                                s = s.substring(0, l7) + getStringValue(extractInterfaceValues(child, 1)) + s.substring(l7 + 2);
                            } while (true);
                            do {
                                int i8 = s.indexOf("%3");
                                if (i8 == -1) {
                                    break;
                                }
                                s = s.substring(0, i8) + getStringValue(extractInterfaceValues(child, 2)) + s.substring(i8 + 2);
                            } while (true);
                            do {
                                int j8 = s.indexOf("%4");
                                if (j8 == -1) {
                                    break;
                                }
                                s = s.substring(0, j8) + getStringValue(extractInterfaceValues(child, 3)) + s.substring(j8 + 2);
                            } while (true);
                            do {
                                int k8 = s.indexOf("%5");
                                if (k8 == -1) {
                                    break;
                                }
                                s = s.substring(0, k8) + getStringValue(extractInterfaceValues(child, 4)) + s.substring(k8 + 2);
                            } while (true);
                        }
                        int l8 = s.indexOf("\\n");
                        String s1;
                        if (l8 != -1) {
                            s1 = s.substring(0, l8);
                            s = s.substring(l8 + 2);
                        } else {
                            s1 = s;
                            s = "";
                        }
                        if (child.aBoolean223) {
                            font.drawCenteredText(i4, k2 + child.width / 2, s1, l6, child.aBoolean268);
                        } else {
                            font.drawText(child.aBoolean268, k2, i4, s1, l6);
                        }
                    }
                } else if (child.interfaceType == 5) {
                    Sprite sprite;
                    if (interfaceEnabled(child)) {
                        sprite = child.sprite2;
                    } else {
                        sprite = child.sprite1;
                    }
                    if (sprite != null) {
                        sprite.drawSprite(k2, l2);
                    }
                } else if (child.interfaceType == 6) {
                    int k3 = Rasterizer.centerX;
                    int j4 = Rasterizer.centerY;
                    Rasterizer.centerX = k2 + child.width / 2;
                    Rasterizer.centerY = l2 + child.height / 2;
                    int i5 = Rasterizer.sineTable[child.anInt270] * child.anInt269 >> 16;
                    int l5 = Rasterizer.cosineTable[child.anInt270] * child.anInt269 >> 16;
                    boolean flag2 = interfaceEnabled(child);
                    int i7;
                    if (flag2) {
                        i7 = child.anInt258;
                    } else {
                        i7 = child.anInt257;
                    }
                    Model model;
                    if (i7 == -1) {
                        model = child.method209(-1, -1, flag2);
                    } else {
                        Sequence sequence = Sequence.sequenceCache[i7];
                        model = child.method209(sequence.anIntArray354[child.anInt246], sequence.frames[child.anInt246], flag2);
                    }
                    if (model != null) {
                        model.method482(child.anInt271, 0, child.anInt270, 0, i5, l5);
                    }
                    Rasterizer.centerX = k3;
                    Rasterizer.centerY = j4;
                } else if (child.interfaceType == 7) {
                    Font font_1 = child.fonts;
                    int k4 = 0;
                    for (int j5 = 0; j5 < child.height; j5++) {
                        for (int i6 = 0; i6 < child.width; i6++) {
                            if (child.inv[k4] > 0) {
                                ItemDefinition itemDefinition = ItemDefinition.forId(child.inv[k4] - 1);
                                String s2 = itemDefinition.name;
                                if (itemDefinition.stackable || child.invStackSizes[k4] != 1) {
                                    s2 = s2 + " x" + Client.getFormattedValueName(child.invStackSizes[k4]);
                                }
                                int i9 = k2 + i6 * (115 + child.invSpritePadX);
                                int k9 = l2 + j5 * (12 + child.invSpritePadY);
                                if (child.aBoolean223) {
                                    font_1.drawCenteredText(child.textColor, i9 + child.width / 2, s2, k9, child.aBoolean268);
                                } else {
                                    font_1.drawText(child.aBoolean268, i9, child.textColor, s2, k9);
                                }
                            }
                            k4++;
                        }
                    }
                }
            }
        }
        Graphics2D.setBounds(l1, i1, k1, j1);
    }

    private void randomizeIndexedSprite(IndexedSprite indexedSprite) {
        int j = 256;
        for (int k = 0; k < anIntArray1190.length; k++) {
            anIntArray1190[k] = 0;
        }
        for (int l = 0; l < 5000; l++) {
            int i1 = (int) (Math.random() * 128D * (double) j);
            anIntArray1190[i1] = (int) (Math.random() * 256D);
        }
        for (int j1 = 0; j1 < 20; j1++) {
            for (int k1 = 1; k1 < j - 1; k1++) {
                for (int i2 = 1; i2 < 127; i2++) {
                    int k2 = i2 + (k1 << 7);
                    anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128] + anIntArray1190[k2 + 128]) / 4;
                }
            }
            int ai[] = anIntArray1190;
            anIntArray1190 = anIntArray1191;
            anIntArray1191 = ai;
        }
        if (indexedSprite != null) {
            int l1 = 0;
            for (int j2 = 0; j2 < indexedSprite.height; j2++) {
                for (int l2 = 0; l2 < indexedSprite.width; l2++) {
                    if (indexedSprite.pixels[l1++] != 0) {
                        int i3 = l2 + 16 + indexedSprite.offsetX;
                        int j3 = j2 + 16 + indexedSprite.offsetY;
                        int k3 = i3 + (j3 << 7);
                        anIntArray1190[k3] = 0;
                    }
                }
            }
        }
    }

    private void parsePlayerUpdateFlag(int flag, int index, Stream updateBuffer, Player player) { // method107
        if ((flag & 0x400) != 0) {
            player.anInt1543 = updateBuffer.getUnsignedByteS();
            player.anInt1545 = updateBuffer.getUnsignedByteS();
            player.anInt1544 = updateBuffer.getUnsignedByteS();
            player.anInt1546 = updateBuffer.getUnsignedByteS();
            player.anInt1547 = updateBuffer.getUnsignedShortA() + Client.loopCycle;
            player.anInt1548 = updateBuffer.getUnsignedLEShortA() + Client.loopCycle;
            player.anInt1549 = updateBuffer.getUnsignedByteS();
            player.method446();
        }
        if ((flag & 0x100) != 0) {
            player.currentGraphic = updateBuffer.getUnsignedLEShort();
            int k = updateBuffer.getInt();
            player.anInt1524 = k >> 16;
            player.anInt1523 = Client.loopCycle + (k & 0xffff);
            player.anInt1521 = 0;
            player.anInt1522 = 0;
            if (player.anInt1523 > Client.loopCycle) {
                player.anInt1521 = -1;
            }
            if (player.currentGraphic == 65535) {
                player.currentGraphic = -1;
            }
        }
        if ((flag & 8) != 0) {
            int l = updateBuffer.getUnsignedLEShort();
            if (l == 65535) {
                l = -1;
            }
            int i2 = updateBuffer.getUnsignedByteC();
            if (l == player.animationId && l != -1) {
                int i3 = Sequence.sequenceCache[l].anInt365;
                if (i3 == 1) {
                    player.anInt1527 = 0;
                    player.anInt1528 = 0;
                    player.animationDelay = i2;
                    player.anInt1530 = 0;
                }
                if (i3 == 2) {
                    player.anInt1530 = 0;
                }
            } else if (l == -1 || player.animationId == -1 || Sequence.sequenceCache[l].anInt359 >= Sequence.sequenceCache[player.animationId].anInt359) {
                player.animationId = l;
                player.anInt1527 = 0;
                player.anInt1528 = 0;
                player.animationDelay = i2;
                player.anInt1530 = 0;
                player.anInt1542 = player.walkQueueLocationIndex;
            }
        }
        if ((flag & 4) != 0) {
            player.textSpoken = updateBuffer.getString();
            if (player.textSpoken.charAt(0) == '~') {
                player.textSpoken = player.textSpoken.substring(1);
                pushMessage(player.textSpoken, 2, player.name);
            } else if (player == Client.myPlayer) {
                pushMessage(player.textSpoken, 2, player.name);
            }
            player.textColor = 0;
            player.textEffects = 0;
            player.textCycle = 150;
        }
        if ((flag & 0x80) != 0) {
            int i1 = updateBuffer.getUnsignedLEShort();
            int j2 = updateBuffer.getUnsignedByte();
            int j3 = updateBuffer.getUnsignedByteC();
            int k3 = updateBuffer.offset;
            if (player.name != null && player.visible) {
                long l3 = TextUtil.nameToLong(player.name);
                boolean bool = false;
                if (j2 <= 1) {
                    for (int i4 = 0; i4 < ignoreCount; i4++) {
                        if (ignoreListAsLongs[i4] != l3) {
                            continue;
                        }
                        bool = true;
                        break;
                    }
                }
                if (!bool && anInt1251 == 0) {
                    try {
                        aStream_834.offset = 0;
                        updateBuffer.writeBytesC(j3, 0, aStream_834.payload);
                        aStream_834.offset = 0;
                        String s = PlayerInput.unpackMessage(j3, aStream_834);
                        s = Censor.doCensor(s);
                        player.textSpoken = s;
                        player.textColor = i1 >> 8;
                        player.privelage = j2;
                        // entityMessage(player);
                        player.textEffects = i1 & 0xff;
                        player.textCycle = 150;
                        if (j2 == 2 || j2 == 3) {
                            pushMessage(s, 1, "@cr2@" + player.name);
                        } else if (j2 == 1) {
                            pushMessage(s, 1, "@cr1@" + player.name);
                        } else {
                            pushMessage(s, 2, player.name);
                        }
                    } catch (Exception exception) {
                        Signlink.reporterror("cde2");
                    }
                }
            }
            updateBuffer.offset = k3 + j3;
        }
        if ((flag & 1) != 0) {
            player.interactingEntity = updateBuffer.getUnsignedLEShort();
            if (player.interactingEntity == 65535) {
                player.interactingEntity = -1;
            }
        }
        if ((flag & 0x10) != 0) {
            int j1 = updateBuffer.getUnsignedByteC();
            byte abyte0[] = new byte[j1];
            Stream stream_1 = new Stream(abyte0);
            updateBuffer.getBytes(j1, 0, abyte0);
            playerAppearanceBuffers[index] = stream_1;
            player.updatePlayer(stream_1);
        }
        if ((flag & 2) != 0) {
            player.anInt1538 = updateBuffer.getUnsignedShortA();
            player.anInt1539 = updateBuffer.getUnsignedLEShort();
        }
        if ((flag & 0x20) != 0) {
            int k1 = updateBuffer.getUnsignedByte();
            int k2 = updateBuffer.getUnsignedByteA();
            player.addHit(k2, k1, Client.loopCycle);
            player.combatCycle = Client.loopCycle + 300;
            player.currentHealth = updateBuffer.getUnsignedByteC();
            player.maxHealth = updateBuffer.getUnsignedByte();
        }
        if ((flag & 0x200) != 0) {
            int l1 = updateBuffer.getUnsignedByte();
            int l2 = updateBuffer.getUnsignedByteS();
            player.addHit(l2, l1, Client.loopCycle);
            player.combatCycle = Client.loopCycle + 300;
            player.currentHealth = updateBuffer.getUnsignedByte();
            player.maxHealth = updateBuffer.getUnsignedByteC();
        }
    }

    private void processCameraRotation() { // method108
        try {
            int x = Client.myPlayer.x + anInt1278;
            int y = Client.myPlayer.y + anInt1131;
            if (anInt1014 - x < -500 || anInt1014 - x > 500 || anInt1015 - y < -500 || anInt1015 - y > 500) {
                anInt1014 = x;
                anInt1015 = y;
            }
            if (anInt1014 != x) {
                anInt1014 += (x - anInt1014) / 16;
            }
            if (anInt1015 != y) {
                anInt1015 += (y - anInt1015) / 16;
            }
            if (super.heldKeys[1] == 1) {
                anInt1186 += (-24 - anInt1186) / 2;
            } else if (super.heldKeys[2] == 1) {
                anInt1186 += (24 - anInt1186) / 2;
            } else {
                anInt1186 /= 2;
            }
            if (super.heldKeys[3] == 1) {
                anInt1187 += (12 - anInt1187) / 2;
            } else if (super.heldKeys[4] == 1) {
                anInt1187 += (-12 - anInt1187) / 2;
            } else {
                anInt1187 /= 2;
            }
            cameraX = cameraX + anInt1186 / 2 & 0x7ff;
            cameraY += anInt1187 / 2;
            if (cameraY < 128) {
                cameraY = 128;
            }
            if (cameraY > 383) {
                cameraY = 383;
            }
            int l = anInt1014 >> 7;
            int i1 = anInt1015 >> 7;
            int j1 = method42(plane, anInt1015, anInt1014);
            int k1 = 0;
            if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
                for (int l1 = l - 4; l1 <= l + 4; l1++) {
                    for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
                        int l2 = plane;
                        if (l2 < 3 && (byteGroundArray[1][l1][k2] & 2) == 2) {
                            l2++;
                        }
                        int i3 = j1 - intGroundArray[l2][l1][k2];
                        if (i3 > k1) {
                            k1 = i3;
                        }
                    }
                }
            }
            Client.anInt1005++;
            if (Client.anInt1005 > 1512) {
                Client.anInt1005 = 0;
                outputStream.writeOpcode(77);
                outputStream.writeByte(0);
                int i2 = outputStream.offset;
                outputStream.writeByte((int) (Math.random() * 256D));
                outputStream.writeByte(101);
                outputStream.writeByte(233);
                outputStream.writeShort(45092);
                if ((int) (Math.random() * 2D) == 0) {
                    outputStream.writeShort(35784);
                }
                outputStream.writeByte((int) (Math.random() * 256D));
                outputStream.writeByte(64);
                outputStream.writeByte(38);
                outputStream.writeShort((int) (Math.random() * 65536D));
                outputStream.writeShort((int) (Math.random() * 65536D));
                outputStream.writeSizeByte(outputStream.offset - i2);
            }
            int j2 = k1 * 192;
            if (j2 > 0x17f00) {
                j2 = 0x17f00;
            }
            if (j2 < 32768) {
                j2 = 32768;
            }
            if (j2 > anInt984) {
                anInt984 += (j2 - anInt984) / 24;
                return;
            }
            if (j2 < anInt984) {
                anInt984 += (j2 - anInt984) / 80;
            }
        } catch (Exception exception) {
            Signlink.reporterror("glfcexception " + Client.myPlayer.x + "," + Client.myPlayer.y + "," + anInt1014 + "," + anInt1015 + "," + anInt1069 + "," + anInt1070 + "," + baseX + "," + baseY);
            throw new RuntimeException("eek");
        }
    }

    public void processDrawing() {
        if (alreadyLoaded || loadingError || genericLoadingError) {
            showErrorScreen();
            return;
        }
        Client.drawCycle++;
        if (!loggedIn) {
            drawTitlebox(false);
        } else {
            drawGameScreen();
        }
        anInt1213 = 0;
    }

    private boolean isFriendOrSelf(String name) {
        if (name == null) {
            return false;
        }
        for (int i = 0; i < friendsCount; i++) {
            if (name.equalsIgnoreCase(friendsNames[i])) {
                return true;
            }
        }
        return name.equalsIgnoreCase(Client.myPlayer.name);
    }

    private static String getCombatLevelColor(int mine, int theirs) {
        int k = mine - theirs;
        if (k < -9) {
            return "@red@";
        }
        if (k < -6) {
            return "@or3@";
        }
        if (k < -3) {
            return "@or2@";
        }
        if (k < 0) {
            return "@or1@";
        }
        if (k > 9) {
            return "@gre@";
        }
        if (k > 6) {
            return "@gr3@";
        }
        if (k > 3) {
            return "@gr2@";
        }
        if (k > 0) {
            return "@gr1@";
        } else {
            return "@yel@";
        }
    }

    private void setWaveVolume(int volume) {
        Signlink.wavevol = volume;
    }

    private void draw3dScreen() {
        drawSplitPrivateChat();
        if (crossState == 1) {
            crosses[crossIndex / 100].drawSprite(crossX - 8 - 4, crossY - 8 - 4);
            Client.anInt1142++;
            if (Client.anInt1142 > 67) {
                Client.anInt1142 = 0;
                outputStream.writeOpcode(78);
            }
        }
        if (crossState == 2) {
            crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - 4, crossY - 8 - 4);
        }
        if (anInt1018 != -1) {
            method119(anInt945, anInt1018);
            drawInterface(0, 0, Interface.cachedInterfaces[anInt1018], 0);
        }
        if (openInterfaceId != -1) {
            method119(anInt945, openInterfaceId);
            drawInterface(0, 0, Interface.cachedInterfaces[openInterfaceId], 0);
        }
        method70();
        if (!menuOpen) {
            processRightClick();
            drawTooltip();
        } else if (menuScreenArea == 0) {
            drawMenu();
        }
        if (anInt1055 == 1) {
            // headIcons[1].drawSprite(472, 296);
            // overlay_multiway
        }
        if (Client.fpsIsOn) {
            int color = 0xffff00;
            if (super.fps < 15) {
                color = 0xff0000;
            }
            aFont_1271.method380("Fps:" + super.fps, 507, color, 20);
            Runtime runtime = Runtime.getRuntime();
            int mem = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
            color = 0xffff00;
            if (mem > 0x2000000 && Client.lowMem) {
                color = 0xff0000;
            }
            aFont_1271.method380("Mem:" + mem + "k", 507, 0xffff00, 35);
        }
        if (systemUpdateTime != 0) {
            int seconds = systemUpdateTime / 50;
            int minutes = seconds / 60;
            seconds %= 60;
            if (seconds < 10) {
                aFont_1271.drawText(0xffff00, "System update in: " + minutes + ":0" + seconds, 329, 4);
            } else {
                aFont_1271.drawText(0xffff00, "System update in: " + minutes + ":" + seconds, 329, 4);
            }
            Client.anInt849++;
            if (Client.anInt849 > 75) {
                Client.anInt849 = 0;
                outputStream.writeOpcode(148);
            }
        }
    }

    private void addIgnore(long encodedName) {
        try {
            if (encodedName == 0L) {
                return;
            }
            if (ignoreCount >= 100) {
                pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
                return;
            }
            String s = TextUtil.formatName(TextUtil.longToName(encodedName));
            for (int j = 0; j < ignoreCount; j++) {
                if (ignoreListAsLongs[j] == encodedName) {
                    pushMessage(s + " is already on your ignore list", 0, "");
                    return;
                }
            }
            for (int k = 0; k < friendsCount; k++) {
                if (friendsNamesAsLongs[k] == encodedName) {
                    pushMessage("Please remove " + s + " from your friend list first", 0, "");
                    return;
                }
            }
            ignoreListAsLongs[ignoreCount++] = encodedName;
            needDrawTabArea = true;
            outputStream.writeOpcode(133);
            outputStream.writeLong(encodedName);
            return;
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("45688, " + encodedName + ", " + 4 + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private void method114() {
        for (int i = -1; i < localPlayerCount; i++) {
            int index;
            if (i == -1) {
                index = myPlayerIndex;
            } else {
                index = playerIndices[i];
            }
            Player player = localPlayers[index];
            if (player != null) {
                method96(player);
            }
        }
    }

    private void method115() {
        if (loadingStage == 2) {
            for (SceneObject object = (SceneObject) aClass19_1179.getFront(); object != null; object = (SceneObject) aClass19_1179.getNext()) {
                if (object.anInt1294 > 0) {
                    object.anInt1294--;
                }
                if (object.anInt1294 == 0) {
                    if (object.anInt1299 < 0 || Region.method178(object.anInt1299, object.anInt1301)) {
                        method142(object.anInt1298, object.anInt1295, object.anInt1300, object.anInt1301, object.anInt1297, object.anInt1296, object.anInt1299);
                        object.unlink();
                    }
                } else {
                    if (object.anInt1302 > 0) {
                        object.anInt1302--;
                    }
                    if (object.anInt1302 == 0 && object.anInt1297 >= 1 && object.anInt1298 >= 1 && object.anInt1297 <= 102 && object.anInt1298 <= 102 && (object.anInt1291 < 0 || Region.method178(object.anInt1291, object.anInt1293))) {
                        method142(object.anInt1298, object.anInt1295, object.anInt1292, object.anInt1293, object.anInt1297, object.anInt1296, object.anInt1291);
                        object.anInt1302 = -1;
                        if (object.anInt1291 == object.anInt1299 && object.anInt1299 == -1) {
                            object.unlink();
                        } else if (object.anInt1291 == object.anInt1299 && object.anInt1292 == object.anInt1300 && object.anInt1293 == object.anInt1301) {
                            object.unlink();
                        }
                    }
                }
            }
        }
    }

    private void determineMenuSize() {
        int i = boldFont.getWidth("Choose Option");
        for (int j = 0; j < menuActionCount; j++) {
            int k = boldFont.getWidth(menuActionName[j]);
            if (k > i) {
                i = k;
            }
        }
        i += 8;
        int l = 15 * menuActionCount + 21;
        if (super.saveClickX > 4 && super.saveClickY > 4 && super.saveClickX < 516 && super.saveClickY < 338) {
            int i1 = super.saveClickX - 4 - i / 2;
            if (i1 + i > 512) {
                i1 = 512 - i;
            }
            if (i1 < 0) {
                i1 = 0;
            }
            int l1 = super.saveClickY - 4;
            if (l1 + l > 334) {
                l1 = 334 - l;
            }
            if (l1 < 0) {
                l1 = 0;
            }
            menuOpen = true;
            menuScreenArea = 0;
            menuOffsetX = i1;
            menuOffsetY = l1;
            menuWidth = i;
            anInt952 = 15 * menuActionCount + 22;
        }
        if (super.saveClickX > 553 && super.saveClickY > 205 && super.saveClickX < 743 && super.saveClickY < 466) {
            int j1 = super.saveClickX - 553 - i / 2;
            if (j1 < 0) {
                j1 = 0;
            } else if (j1 + i > 190) {
                j1 = 190 - i;
            }
            int i2 = super.saveClickY - 205;
            if (i2 < 0) {
                i2 = 0;
            } else if (i2 + l > 261) {
                i2 = 261 - l;
            }
            menuOpen = true;
            menuScreenArea = 1;
            menuOffsetX = j1;
            menuOffsetY = i2;
            menuWidth = i;
            anInt952 = 15 * menuActionCount + 22;
        }
        if (super.saveClickX > 17 && super.saveClickY > 357 && super.saveClickX < 496 && super.saveClickY < 453) {
            int k1 = super.saveClickX - 17 - i / 2;
            if (k1 < 0) {
                k1 = 0;
            } else if (k1 + i > 479) {
                k1 = 479 - i;
            }
            int j2 = super.saveClickY - 357;
            if (j2 < 0) {
                j2 = 0;
            } else if (j2 + l > 96) {
                j2 = 96 - l;
            }
            menuOpen = true;
            menuScreenArea = 2;
            menuOffsetX = k1;
            menuOffsetY = j2;
            menuWidth = i;
            anInt952 = 15 * menuActionCount + 22;
        }
    }

    private void updateLocalPlayerMovement(Stream updateBuffer) { // method117
        updateBuffer.beginBitBlock();
        int flag = updateBuffer.getBits(1);
        if (flag == 0) {
            return;
        }
        int type = updateBuffer.getBits(2);
        if (type == 0) {
            playerUpdateIndices[entityUpdateCount++] = myPlayerIndex;
            return;
        }
        if (type == 1) {
            int dir = updateBuffer.getBits(3);
            Client.myPlayer.move(false, dir);
            int update = updateBuffer.getBits(1);
            if (update == 1) {
                playerUpdateIndices[entityUpdateCount++] = myPlayerIndex;
            }
            return;
        }
        if (type == 2) {
            int walk = updateBuffer.getBits(3);
            Client.myPlayer.move(true, walk);
            int run = updateBuffer.getBits(3);
            Client.myPlayer.move(true, run);
            int update = updateBuffer.getBits(1);
            if (update == 1) {
                playerUpdateIndices[entityUpdateCount++] = myPlayerIndex;
            }
            return;
        }
        if (type == 3) {
            plane = updateBuffer.getBits(2);
            int clearQueue = updateBuffer.getBits(1);
            int update = updateBuffer.getBits(1);
            if (update == 1) {
                playerUpdateIndices[entityUpdateCount++] = myPlayerIndex;
            }
            int offX = updateBuffer.getBits(7);
            int offY = updateBuffer.getBits(7);
            Client.myPlayer.updatePosition(offY, offX, clearQueue == 1);
        }
    }

    private void dispose() {
        aBoolean831 = false;
        while (drawingFlames) {
            aBoolean831 = false;
            try {
                Thread.sleep(50L);
            } catch (Exception exception) {
            }
        }
        titleBox = null;
        titleButton = null;
        runes = null;
        anIntArray850 = null;
        anIntArray851 = null;
        anIntArray852 = null;
        anIntArray853 = null;
        anIntArray1190 = null;
        anIntArray1191 = null;
        anIntArray828 = null;
        anIntArray829 = null;
        aClass30_Sub2_Sub1_Sub1_1201 = null;
        aClass30_Sub2_Sub1_Sub1_1202 = null;
    }

    private boolean method119(int i, int frame) {
        boolean flag1 = false;
        Interface parent = Interface.cachedInterfaces[frame];
        for (int element : parent.children) {
            if (element == -1) {
                break;
            }
            Interface child = Interface.cachedInterfaces[element];
            if (child.interfaceType == 1) {
                flag1 |= method119(i, child.id);
            }
            if (child.interfaceType == 6 && (child.anInt257 != -1 || child.anInt258 != -1)) {
                boolean flag2 = interfaceEnabled(child);
                int l;
                if (flag2) {
                    l = child.anInt258;
                } else {
                    l = child.anInt257;
                }
                if (l != -1) {
                    Sequence sequence = Sequence.sequenceCache[l];
                    for (child.anInt208 += i; child.anInt208 > sequence.getFrameLength(child.anInt246);) {
                        child.anInt208 -= sequence.getFrameLength(child.anInt246) + 1;
                        child.anInt246++;
                        if (child.anInt246 >= sequence.length) {
                            child.anInt246 -= sequence.frameStep;
                            if (child.anInt246 < 0 || child.anInt246 >= sequence.length) {
                                child.anInt246 = 0;
                            }
                        }
                        flag1 = true;
                    }
                }
            }
        }
        return flag1;
    }

    private int method120() {
        int j = 3;
        if (yCameraCurve < 310) {
            int k = xCameraPos >> 7;
            int l = yCameraPos >> 7;
            int i1 = Client.myPlayer.x >> 7;
            int j1 = Client.myPlayer.y >> 7;
            if ((byteGroundArray[plane][k][l] & 4) != 0) {
                j = plane;
            }
            int k1;
            if (i1 > k) {
                k1 = i1 - k;
            } else {
                k1 = k - i1;
            }
            int l1;
            if (j1 > l) {
                l1 = j1 - l;
            } else {
                l1 = l - j1;
            }
            if (k1 > l1) {
                int i2 = l1 * 0x10000 / k1;
                int k2 = 32768;
                while (k != i1) {
                    if (k < i1) {
                        k++;
                    } else if (k > i1) {
                        k--;
                    }
                    if ((byteGroundArray[plane][k][l] & 4) != 0) {
                        j = plane;
                    }
                    k2 += i2;
                    if (k2 >= 0x10000) {
                        k2 -= 0x10000;
                        if (l < j1) {
                            l++;
                        } else if (l > j1) {
                            l--;
                        }
                        if ((byteGroundArray[plane][k][l] & 4) != 0) {
                            j = plane;
                        }
                    }
                }
            } else {
                int j2 = k1 * 0x10000 / l1;
                int l2 = 32768;
                while (l != j1) {
                    if (l < j1) {
                        l++;
                    } else if (l > j1) {
                        l--;
                    }
                    if ((byteGroundArray[plane][k][l] & 4) != 0) {
                        j = plane;
                    }
                    l2 += j2;
                    if (l2 >= 0x10000) {
                        l2 -= 0x10000;
                        if (k < i1) {
                            k++;
                        } else if (k > i1) {
                            k--;
                        }
                        if ((byteGroundArray[plane][k][l] & 4) != 0) {
                            j = plane;
                        }
                    }
                }
            }
        }
        if ((byteGroundArray[plane][Client.myPlayer.x >> 7][Client.myPlayer.y >> 7] & 4) != 0) {
            j = plane;
        }
        return j;
    }

    private int method121() {
        int j = method42(plane, yCameraPos, xCameraPos);
        if (j - zCameraPos < 800 && (byteGroundArray[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0) {
            return plane;
        } else {
            return 3;
        }
    }

    private void delIgnore(long encodedName) {
        try {
            if (encodedName == 0L) {
                return;
            }
            for (int id = 0; id < ignoreCount; id++) {
                if (ignoreListAsLongs[id] == encodedName) {
                    ignoreCount--;
                    needDrawTabArea = true;
                    System.arraycopy(ignoreListAsLongs, id + 1, ignoreListAsLongs, id, ignoreCount - id);
                    outputStream.writeOpcode(74);
                    outputStream.writeLong(encodedName);
                    return;
                }
            }
            return;
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("47229, " + 3 + ", " + encodedName + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    public String getParameter(String s) {
        if (Signlink.mainapp != null) {
            return Signlink.mainapp.getParameter(s);
        } else {
            return super.getParameter(s);
        }
    }

    private void adjustVolume(boolean volumeOn, int volume) {
        Signlink.midivol = volume;
        if (volumeOn) {
            Signlink.midi = "voladjust";
        }
    }

    private int extractInterfaceValues(Interface component, int id) {
        if (component.valueIndices == null || id >= component.valueIndices.length) {
            return -2;
        }
        try {
            int values[] = component.valueIndices[id];
            int k = 0;
            int off = 0;
            int i1 = 0;
            do {
                int j1 = values[off++];
                int k1 = 0;
                byte byte0 = 0;
                if (j1 == 0) {
                    return k;
                }
                if (j1 == 1) {
                    k1 = currentStats[values[off++]];
                }
                if (j1 == 2) {
                    k1 = maxStats[values[off++]];
                }
                if (j1 == 3) {
                    k1 = currentExp[values[off++]];
                }
                if (j1 == 4) {
                    Interface instance = Interface.cachedInterfaces[values[off++]];
                    int k2 = values[off++];
                    if (k2 >= 0 && k2 < ItemDefinition.itemCount && (!ItemDefinition.forId(k2).membersObject || Client.isMembers)) {
                        for (int j3 = 0; j3 < instance.inv.length; j3++) {
                            if (instance.inv[j3] == k2 + 1) {
                                k1 += instance.invStackSizes[j3];
                            }
                        }
                    }
                }
                if (j1 == 5) {
                    k1 = configStates[values[off++]];
                }
                if (j1 == 6) {
                    k1 = Client.anIntArray1019[maxStats[values[off++]] - 1];
                }
                if (j1 == 7) {
                    k1 = configStates[values[off++]] * 100 / 46875;
                }
                if (j1 == 8) {
                    k1 = Client.myPlayer.combatLevel;
                }
                if (j1 == 9) {
                    for (int l1 = 0; l1 < Skills.skillsCount; l1++) {
                        if (Skills.skillEnabled[l1]) {
                            k1 += maxStats[l1];
                        }
                    }
                }
                if (j1 == 10) {
                    Interface instance = Interface.cachedInterfaces[values[off++]];
                    int l2 = values[off++] + 1;
                    if (l2 >= 0 && l2 < ItemDefinition.itemCount && (!ItemDefinition.forId(l2).membersObject || Client.isMembers)) {
                        for (int element : instance.inv) {
                            if (element != l2) {
                                continue;
                            }
                            k1 = 0x3b9ac9ff;
                            break;
                        }
                    }
                }
                if (j1 == 11) {
                    k1 = energy;
                }
                if (j1 == 12) {
                    k1 = weight;
                }
                if (j1 == 13) {
                    int i2 = configStates[values[off++]];
                    int i3 = values[off++];
                    k1 = (i2 & 1 << i3) == 0 ? 0 : 1;
                }
                if (j1 == 14) {
                    int j2 = values[off++];
                    VarBit varBit = VarBit.cache[j2];
                    int l3 = varBit.configId;
                    int i4 = varBit.anInt649;
                    int j4 = varBit.anInt650;
                    int k4 = Client.anIntArray1232[j4 - i4];
                    k1 = configStates[l3] >> i4 & k4;
                }
                if (j1 == 15) {
                    byte0 = 1;
                }
                if (j1 == 16) {
                    byte0 = 2;
                }
                if (j1 == 17) {
                    byte0 = 3;
                }
                if (j1 == 18) {
                    k1 = (Client.myPlayer.x >> 7) + baseX;
                }
                if (j1 == 19) {
                    k1 = (Client.myPlayer.y >> 7) + baseY;
                }
                if (j1 == 20) {
                    k1 = values[off++];
                }
                if (byte0 == 0) {
                    if (i1 == 0) {
                        k += k1;
                    }
                    if (i1 == 1) {
                        k -= k1;
                    }
                    if (i1 == 2 && k1 != 0) {
                        k /= k1;
                    }
                    if (i1 == 3) {
                        k *= k1;
                    }
                    i1 = 0;
                } else {
                    i1 = byte0;
                }
            } while (true);
        } catch (Exception exception) {
            return -1;
        }
    }

    private void drawTooltip() {
        if (menuActionCount < 2 && itemSelected == 0 && spellSelected == 0) {
            return;
        }
        String text;
        if (itemSelected == 1 && menuActionCount < 2) {
            text = "Use " + selectedItemName + " with...";
        } else if (spellSelected == 1 && menuActionCount < 2) {
            text = spellTooltip + "...";
        } else {
            text = menuActionName[menuActionCount - 1];
        }
        if (menuActionCount > 2) {
            text = text + "@whi@ / " + (menuActionCount - 2) + " more options";
        }
        boldFont.method390(4, 0xffffff, text, Client.loopCycle / 1000, 15);
    }

    private void drawMinimap() {
        minimapCanvas.initDrawingArea();
        if (anInt1021 == 2) {
            byte src[] = mapBack.pixels;
            int dest[] = Graphics2D.pixels;
            for (int toReset = 0; toReset < src.length; toReset++) {
                if (src[toReset] == 0) {
                    dest[toReset] = 0;
                }
            }
            compass.drawShapedSprite(33, cameraX, anIntArray1057, 256, anIntArray968, 25, 0, 0, 33, 25);
            gameScreenCanvas.initDrawingArea();
            return;
        }
        int i = cameraX + minimapInt2 & 0x7ff;
        int j = 48 + Client.myPlayer.x / 32;
        int l2 = 464 - Client.myPlayer.y / 32;
        minimap.drawShapedSprite(151, i, anIntArray1229, 256 + minimapInt3, anIntArray1052, l2, 5, 25, 146, j);
        compass.drawShapedSprite(33, cameraX, anIntArray1057, 256, anIntArray968, 25, 0, 0, 33, 25);
        for (int j5 = 0; j5 < anInt1071; j5++) {
            int k = anIntArray1072[j5] * 4 + 2 - Client.myPlayer.x / 32;
            int i3 = anIntArray1073[j5] * 4 + 2 - Client.myPlayer.y / 32;
            markMinimap(aClass30_Sub2_Sub1_Sub1Array1140[j5], k, i3);
        }
        for (int baseX = 0; baseX < 104; baseX++) {
            for (int baseY = 0; baseY < 104; baseY++) {
                Deque groundItem = groundItems[plane][baseX][baseY];
                if (groundItem != null) {
                    int x = baseX * 4 + 2 - Client.myPlayer.x / 32;
                    int y = baseY * 4 + 2 - Client.myPlayer.y / 32;
                    markMinimap(mapDotItem, x, y);
                }
            }
        }
        for (int ptr = 0; ptr < localNpcCount; ptr++) {
            Npc npc = localNpcs[localNpcIndices[ptr]];
            if (npc != null && npc.isVisible()) {
                NpcDefinition npcDefinition = npc.desc;
                if (npcDefinition.childIds != null) {
                    npcDefinition = npcDefinition.method161();
                }
                if (npcDefinition != null && npcDefinition.visibleOnMinimap && npcDefinition.clickable) {
                    int x = npc.x / 32 - Client.myPlayer.x / 32;
                    int y = npc.y / 32 - Client.myPlayer.y / 32;
                    markMinimap(mapDotNPC, x, y);
                }
            }
        }
        for (int ptr = 0; ptr < localPlayerCount; ptr++) {
            Player player = localPlayers[playerIndices[ptr]];
            if (player != null && player.isVisible()) {
                int x = player.x / 32 - Client.myPlayer.x / 32;
                int y = player.y / 32 - Client.myPlayer.y / 32;
                boolean flag = false;
                long encName = TextUtil.nameToLong(player.name);
                for (int friendId = 0; friendId < friendsCount; friendId++) {
                    if (encName != friendsNamesAsLongs[friendId] || friendsWorlds[friendId] == 0) {
                        continue;
                    }
                    flag = true;
                    break;
                }
                boolean isTeam = false;
                if (Client.myPlayer.team != 0 && player.team != 0 && Client.myPlayer.team == player.team) {
                    isTeam = true;
                }
                if (flag) {
                    markMinimap(mapDotFriend, x, y);
                } else if (isTeam) {
                    markMinimap(mapDotTeam, x, y);
                } else {
                    markMinimap(mapDotPlayer, x, y);
                }
            }
        }
        if (anInt855 != 0 && Client.loopCycle % 20 < 10) {
            if (anInt855 == 1 && anInt1222 >= 0 && anInt1222 < localNpcs.length) {
                Npc npc = localNpcs[anInt1222];
                if (npc != null) {
                    int x = npc.x / 32 - Client.myPlayer.x / 32;
                    int y = npc.y / 32 - Client.myPlayer.y / 32;
                    method81(mapMarker, y, x);
                }
            }
            if (anInt855 == 2) {
                int x = (anInt934 - baseX) * 4 + 2 - Client.myPlayer.x / 32;
                int y = (anInt935 - baseY) * 4 + 2 - Client.myPlayer.y / 32;
                method81(mapMarker, y, x);
            }
            if (anInt855 == 10 && anInt933 >= 0 && anInt933 < localPlayers.length) {
                Player player = localPlayers[anInt933];
                if (player != null) {
                    int x = player.x / 32 - Client.myPlayer.x / 32;
                    int y = player.y / 32 - Client.myPlayer.y / 32;
                    method81(mapMarker, y, x);
                }
            }
        }
        if (destX != 0) {
            int x = destX * 4 + 2 - Client.myPlayer.x / 32;
            int y = destY * 4 + 2 - Client.myPlayer.y / 32;
            markMinimap(mapFlag, x, y);
        }
        Graphics2D.fillRect(3, 78, 97, 0xffffff, 3);
        gameScreenCanvas.initDrawingArea();
    }

    private void worldToScreen(Mobile mobile, int offset) {
        getEntityScreenPos(mobile.x, offset, mobile.y);
    }

    private void getEntityScreenPos(int x, int offset, int y) {
        if (x < 128 || y < 128 || x > 13056 || y > 13056) {
            spriteDrawX = -1;
            spriteDrawY = -1;
            return;
        }
        int i1 = method42(plane, y, x) - offset;
        x -= xCameraPos;
        i1 -= zCameraPos;
        y -= yCameraPos;
        int j1 = Model.modelIntArray1[yCameraCurve];
        int k1 = Model.modelIntArray2[yCameraCurve];
        int l1 = Model.modelIntArray1[xCameraCurve];
        int i2 = Model.modelIntArray2[xCameraCurve];
        int j2 = y * l1 + x * i2 >> 16;
        y = y * i2 - x * l1 >> 16;
        x = j2;
        j2 = i1 * k1 - y * j1 >> 16;
        y = i1 * j1 + y * k1 >> 16;
        i1 = j2;
        if (y >= 50) {
            spriteDrawX = Rasterizer.centerX + (x << 9) / y;
            spriteDrawY = Rasterizer.centerY + (i1 << 9) / y;
        } else {
            spriteDrawX = -1;
            spriteDrawY = -1;
        }
    }

    private void buildSplitPrivateChatMenu() {
        if (splitPrivateChat == 0) {
            return;
        }
        int i = 0;
        if (systemUpdateTime != 0) {
            i = 1;
        }
        for (int j = 0; j < 100; j++) {
            if (chatMessages[j] != null) {
                int k = chatTypes[j];
                String s = chatNames[j];
                if (s != null && s.startsWith("@cr1@")) {
                    s = s.substring(5);
                }
                if (s != null && s.startsWith("@cr2@")) {
                    s = s.substring(5);
                }
                if ((k == 3 || k == 7) && (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
                    int l = 329 - i * 13;
                    if (super.mouseX > 4 && super.mouseY - 4 > l - 10 && super.mouseY - 4 <= l + 3) {
                        int i1 = aFont_1271.getWidth("From:  " + s + chatMessages[j]) + 25;
                        if (i1 > 450) {
                            i1 = 450;
                        }
                        if (super.mouseX < 4 + i1) {
                            if (myPrivilege >= 1) {
                                menuActionName[menuActionCount] = "Report abuse @whi@" + s;
                                menuActionOpcode[menuActionCount] = 2606;
                                menuActionCount++;
                            }
                            menuActionName[menuActionCount] = "Add ignore @whi@" + s;
                            menuActionOpcode[menuActionCount] = 2042;
                            menuActionCount++;
                            menuActionName[menuActionCount] = "Add friend @whi@" + s;
                            menuActionOpcode[menuActionCount] = 2337;
                            menuActionCount++;
                        }
                    }
                    if (++i >= 5) {
                        return;
                    }
                }
                if ((k == 5 || k == 6) && privateChatMode < 2 && ++i >= 5) {
                    return;
                }
            }
        }
    }

    private void method130(int arg0, int id, int rotation, int arg3, int y, int objectType, int plane, int x, int arg8) {
        SceneObject object = null;
        for (SceneObject sceneObject = (SceneObject) aClass19_1179.getFront(); sceneObject != null; sceneObject = (SceneObject) aClass19_1179.getNext()) {
            if (sceneObject.anInt1295 != plane || sceneObject.anInt1297 != x || sceneObject.anInt1298 != y || sceneObject.anInt1296 != arg3) {
                continue;
            }
            object = sceneObject;
            break;
        }
        if (object == null) {
            object = new SceneObject();
            object.anInt1295 = plane;
            object.anInt1296 = arg3;
            object.anInt1297 = x;
            object.anInt1298 = y;
            method89(object);
            aClass19_1179.insertBack(object);
        }
        System.out.println(id + "," + x + "," + y);
        object.anInt1291 = id;
        object.anInt1293 = objectType;
        object.anInt1292 = rotation;
        object.anInt1302 = arg8;
        object.anInt1294 = arg0;
    }

    private boolean interfaceEnabled(Interface component) { // interfaceIsSelected
        if (component.valueCompareType == null) {
            return false;
        }
        for (int i = 0; i < component.valueCompareType.length; i++) {
            int j = extractInterfaceValues(component, i);
            int k = component.requiredValues[i];
            if (component.valueCompareType[i] == 2) {
                if (j >= k) {
                    return false;
                }
            } else if (component.valueCompareType[i] == 3) {
                if (j <= k) {
                    return false;
                }
            } else if (component.valueCompareType[i] == 4) {
                if (j == k) {
                    return false;
                }
            } else if (j != k) {
                return false;
            }
        }
        return true;
    }

    private DataInputStream requestFile(String name) throws IOException {
        // if (!aBoolean872) {
        // if (Signlink.mainapp != null) {
        // return Signlink.openurl(name);
        // } else {
        // return new DataInputStream((new URL(getCodeBase(), name)).openStream());
        // }
        // }
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception exception) {
            }
            socket = null;
        }
        socket = openSocket(43595);
        socket.setSoTimeout(10000);
        java.io.InputStream inputstream = socket.getInputStream();
        OutputStream outputstream = socket.getOutputStream();
        outputstream.write(("JAGGRAB /" + name + "\n\n").getBytes());
        return new DataInputStream(inputstream);
    }

    private void drawFlames() {
        char c = '\u0100';
        if (anInt1040 > 0) {
            for (int i = 0; i < 256; i++) {
                if (anInt1040 > 768) {
                    anIntArray850[i] = method83(anIntArray851[i], anIntArray852[i], 1024 - anInt1040);
                } else if (anInt1040 > 256) {
                    anIntArray850[i] = anIntArray852[i];
                } else {
                    anIntArray850[i] = method83(anIntArray852[i], anIntArray851[i], 256 - anInt1040);
                }
            }
        } else if (anInt1041 > 0) {
            for (int j = 0; j < 256; j++) {
                if (anInt1041 > 768) {
                    anIntArray850[j] = method83(anIntArray851[j], anIntArray853[j], 1024 - anInt1041);
                } else if (anInt1041 > 256) {
                    anIntArray850[j] = anIntArray853[j];
                } else {
                    anIntArray850[j] = method83(anIntArray853[j], anIntArray851[j], 256 - anInt1041);
                }
            }
        } else {
            System.arraycopy(anIntArray851, 0, anIntArray850, 0, 256);
        }
        System.arraycopy(aClass30_Sub2_Sub1_Sub1_1201.pixels, 0, leftFlameCanvas.componentPixels, 0, 33920);
        int i1 = 0;
        int j1 = 1152;
        for (int k1 = 1; k1 < c - 1; k1++) {
            int l1 = anIntArray969[k1] * (c - k1) / c;
            int j2 = 22 + l1;
            if (j2 < 0) {
                j2 = 0;
            }
            i1 += j2;
            for (int l2 = j2; l2 < 128; l2++) {
                int j3 = anIntArray828[i1++];
                if (j3 != 0) {
                    int l3 = j3;
                    int j4 = 256 - j3;
                    j3 = anIntArray850[j3];
                    int l4 = leftFlameCanvas.componentPixels[j1];
                    leftFlameCanvas.componentPixels[j1++] = ((j3 & 0xff00ff) * l3 + (l4 & 0xff00ff) * j4 & 0xff00ff00) + ((j3 & 0xff00) * l3 + (l4 & 0xff00) * j4 & 0xff0000) >> 8;
                } else {
                    j1++;
                }
            }
            j1 += j2;
        }
        leftFlameCanvas.drawGraphics(0, super.graphics, 0);
        System.arraycopy(aClass30_Sub2_Sub1_Sub1_1202.pixels, 0, rightFlameCanvas.componentPixels, 0, 33920);
        i1 = 0;
        j1 = 1176;
        for (int k2 = 1; k2 < c - 1; k2++) {
            int i3 = anIntArray969[k2] * (c - k2) / c;
            int k3 = 103 - i3;
            j1 += i3;
            for (int i4 = 0; i4 < k3; i4++) {
                int k4 = anIntArray828[i1++];
                if (k4 != 0) {
                    int i5 = k4;
                    int j5 = 256 - k4;
                    k4 = anIntArray850[k4];
                    int k5 = rightFlameCanvas.componentPixels[j1];
                    rightFlameCanvas.componentPixels[j1++] = ((k4 & 0xff00ff) * i5 + (k5 & 0xff00ff) * j5 & 0xff00ff00) + ((k4 & 0xff00) * i5 + (k5 & 0xff00) * j5 & 0xff0000) >> 8;
                } else {
                    j1++;
                }
            }
            i1 += 128 - k3;
            j1 += 128 - k3 - i3;
        }
        rightFlameCanvas.drawGraphics(0, super.graphics, 637);
    }

    private void updatePlayerMovement(Stream updateBuffer) { // method134
        int count = updateBuffer.getBits(8);
        if (count < localPlayerCount) {
            for (int id = count; id < localPlayerCount; id++) {
                localPlayerIndices[localEntityCount++] = playerIndices[id];
            }
        }
        if (count > localPlayerCount) {
            Signlink.reporterror(myUsername + " Too many players");
            throw new RuntimeException("eek");
        }
        localPlayerCount = 0;
        for (int id = 0; id < count; id++) {
            int index = playerIndices[id];
            Player player = localPlayers[index];
            int flag = updateBuffer.getBits(1);
            if (flag == 0) {
                playerIndices[localPlayerCount++] = index;
                player.lastUpdate = Client.loopCycle;
            } else {
                int type = updateBuffer.getBits(2);
                if (type == 0) {
                    playerIndices[localPlayerCount++] = index;
                    player.lastUpdate = Client.loopCycle;
                    playerUpdateIndices[entityUpdateCount++] = index;
                } else if (type == 1) {
                    playerIndices[localPlayerCount++] = index;
                    player.lastUpdate = Client.loopCycle;
                    int dir = updateBuffer.getBits(3);
                    player.move(false, dir);
                    int update = updateBuffer.getBits(1);
                    if (update == 1) {
                        playerUpdateIndices[entityUpdateCount++] = index;
                    }
                } else if (type == 2) {
                    playerIndices[localPlayerCount++] = index;
                    player.lastUpdate = Client.loopCycle;
                    int walk = updateBuffer.getBits(3);
                    player.move(true, walk);
                    int run = updateBuffer.getBits(3);
                    player.move(true, run);
                    int update = updateBuffer.getBits(1);
                    if (update == 1) {
                        playerUpdateIndices[entityUpdateCount++] = index;
                    }
                } else if (type == 3) {
                    localPlayerIndices[localEntityCount++] = index;
                }
            }
        }
    }

    private void drawTitlebox(boolean loggedIn) { // drawLoginScreen
        resetImageProducers();
        leftCanvas.initDrawingArea();
        titleBox.drawIndexedSprite(0, 0);
        if (loginScreenState == 0) { // Default
            smallFont.drawCenteredText(0x75a9a9, 180, onDemandFetcher.statusString, 180, true);
            boldFont.drawCenteredText(0xffff00, 180, "Welcome to RuneScape", 80, true);
            titleButton.drawIndexedSprite(27, 100);
            boldFont.drawCenteredText(0xffffff, 100, "New User", 125, true);
            titleButton.drawIndexedSprite(187, 100);
            boldFont.drawCenteredText(0xffffff, 260, "Existing User", 125, true);
        }
        if (loginScreenState == 2) { // Existing User
            if (loginMessage1.length() > 0) {
                boldFont.drawCenteredText(0xffff00, 180, loginMessage1, 45, true);
                boldFont.drawCenteredText(0xffff00, 180, loginMessage2, 60, true);
            } else {
                boldFont.drawCenteredText(0xffff00, 180, loginMessage2, 53, true);
            }
            boldFont.drawText(true, 90, 0xffffff, "Username: " + myUsername + (loginScreenCursorPos == 0 & Client.loopCycle % 40 < 20 ? "@yel@|" : ""), 90);
            boldFont.drawText(true, 92, 0xffffff, "Password: " + TextUtil.censorPassword(myPassword) + (loginScreenCursorPos == 1 & Client.loopCycle % 40 < 20 ? "@yel@|" : ""), 105);
            if (!loggedIn) {
                titleButton.drawIndexedSprite(27, 130);
                boldFont.drawCenteredText(0xffffff, 100, "Login", 155, true);
                titleButton.drawIndexedSprite(187, 130);
                boldFont.drawCenteredText(0xffffff, 260, "Cancel", 155, true);
            }
        }
        if (loginScreenState == 3) { // New User
            boldFont.drawCenteredText(0xffff00, 180, "Create a free account", 40, true);
            boldFont.drawCenteredText(0xffffff, 180, "To create a new account you need to", 65, true);
            boldFont.drawCenteredText(0xffffff, 180, "go back to the main RuneScape webpage", 80, true);
            boldFont.drawCenteredText(0xffffff, 180, "and choose the red 'create account'", 95, true);
            boldFont.drawCenteredText(0xffffff, 180, "button at the top right of that page.", 110, true);
            titleButton.drawIndexedSprite(107, 130);
            boldFont.drawCenteredText(0xffffff, 180, "Cancel", 155, true);
        }
        leftCanvas.drawGraphics(171, super.graphics, 202);
        if (welcomeScreenRaised) {
            welcomeScreenRaised = false;
            topLeftCanvas.drawGraphics(0, super.graphics, 128);
            bottomLeftCanvas.drawGraphics(371, super.graphics, 202);
            titleBoxLeftCanvas.drawGraphics(265, super.graphics, 0);
            bottomRightCanvas.drawGraphics(265, super.graphics, 562);
            smallLeftCanvas.drawGraphics(171, super.graphics, 128);
            smallRightCanvas.drawGraphics(171, super.graphics, 562);
        }
    }

    private void processFlameDrawing() {
        drawingFlames = true;
        try {
            long l = System.currentTimeMillis();
            int i = 0;
            int j = 20;
            while (aBoolean831) {
                flameCycle++;
                getFlameOffsets();
                getFlameOffsets();
                drawFlames();
                if (++i > 10) {
                    long l1 = System.currentTimeMillis();
                    int k = (int) (l1 - l) / 10 - j;
                    j = 40 - k;
                    if (j < 5) {
                        j = 5;
                    }
                    i = 0;
                    l = l1;
                }
                try {
                    Thread.sleep(j);
                } catch (Exception exception) {
                }
            }
        } catch (Exception exception) {
        }
        drawingFlames = false;
    }

    public void raiseWelcomeScreen() {
        welcomeScreenRaised = true;
    }

    private void method137(Stream packetBuffer, int opcode) {
        if (opcode == 84) {
            int loc = packetBuffer.getUnsignedByte();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            int itemId = packetBuffer.getUnsignedShort();
            int srcAmount = packetBuffer.getUnsignedShort();
            int destAmount = packetBuffer.getUnsignedShort();
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                Deque items = groundItems[plane][x][y];
                if (items != null) {
                    for (GroundItem groundItem = (GroundItem) items.getFront(); groundItem != null; groundItem = (GroundItem) items.getNext()) {
                        if (groundItem.id != (itemId & 0x7fff) || groundItem.amount != srcAmount) {
                            continue;
                        }
                        groundItem.amount = destAmount;
                        break;
                    }
                    spawnGroundItem(x, y);
                }
            }
            return;
        }
        if (opcode == 105) {
            int loc = packetBuffer.getUnsignedByte();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            int objectId = packetBuffer.getUnsignedShort();
            int objectData = packetBuffer.getUnsignedByte();
            int objectType = objectData >> 4 & 0xf;
            int objectRot = objectData & 7;
            if (myPlayer.walkQueueX[0] >= x - objectType && myPlayer.walkQueueX[0] <= x + objectType && myPlayer.walkQueueY[0] >= y - objectType && myPlayer.walkQueueY[0] <= y + objectType && aBoolean848 && !lowMem && anInt1062 < 50) {
                anIntArray1207[anInt1062] = objectId;
                anIntArray1241[anInt1062] = objectRot;
                anIntArray1250[anInt1062] = Sound.anIntArray326[objectId];
                anInt1062++;
            }
        }
        if (opcode == 215) {
            int itemId = packetBuffer.getUnsignedLEShortA();
            int loc = packetBuffer.getUnsignedByteS();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            int unknown = packetBuffer.getUnsignedLEShortA();
            int amount = packetBuffer.getUnsignedShort();
            if (x >= 0 && y >= 0 && x < 104 && y < 104 && unknown != unknownInt10) {
                GroundItem groundItem = new GroundItem();
                groundItem.id = itemId;
                groundItem.amount = amount;
                if (groundItems[plane][x][y] == null) {
                    groundItems[plane][x][y] = new Deque();
                }
                groundItems[plane][x][y].insertBack(groundItem);
                spawnGroundItem(x, y);
            }
            return;
        }
        if (opcode == 156) {
            int loc = packetBuffer.getUnsignedByteA();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            int itemId = packetBuffer.getUnsignedShort();
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                Deque items = groundItems[plane][x][y];
                if (items != null) {
                    for (GroundItem groundItem = (GroundItem) items.getFront(); groundItem != null; groundItem = (GroundItem) items.getNext()) {
                        if (groundItem.id != (itemId & 0x7fff)) {
                            continue;
                        }
                        groundItem.unlink();
                        break;
                    }
                    if (items.getFront() == null) {
                        groundItems[plane][x][y] = null;
                    }
                    spawnGroundItem(x, y);
                }
            }
            return;
        }
        if (opcode == 160) {
            int loc = packetBuffer.getUnsignedByteS();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            int objectData = packetBuffer.getUnsignedByteS();
            int objectType = objectData >> 2;
            int objectRot = objectData & 3;
            int j16 = anIntArray1177[objectType];
            int animationId = packetBuffer.getUnsignedLEShortA();
            if (x >= 0 && y >= 0 && x < 103 && y < 103) {
                int j18 = intGroundArray[plane][x][y];
                int i19 = intGroundArray[plane][x + 1][y];
                int l19 = intGroundArray[plane][x + 1][y + 1];
                int k20 = intGroundArray[plane][x][y + 1];
                if (j16 == 0) {
                    WallObject wallObject = sceneGraph.getWallObject(plane, x, y);
                    if (wallObject != null) {
                        int id = wallObject.uid >> 14 & 0x7fff;
                        if (objectType == 2) {
                            wallObject.aEntity_278 = new AnimatedObject(id, 4 + objectRot, 2, i19, l19, j18, k20, animationId, false);
                            wallObject.aEntity_279 = new AnimatedObject(id, objectRot + 1 & 3, 2, i19, l19, j18, k20, animationId, false);
                        } else {
                            wallObject.aEntity_278 = new AnimatedObject(id, objectRot, objectType, i19, l19, j18, k20, animationId, false);
                        }
                    }
                }
                if (j16 == 1) {
                    WallDecoration wallDecoration = sceneGraph.getWallDecoration(x, y, plane);
                    if (wallDecoration != null) {
                        wallDecoration.entity = new AnimatedObject(wallDecoration.uid >> 14 & 0x7fff, 0, 4, i19, l19, j18, k20, animationId, false);
                    }
                }
                if (j16 == 2) {
                    InteractiveObject interactiveObject = sceneGraph.getInteractiveObject(x, y, plane);
                    if (objectType == 11) {
                        objectType = 10;
                    }
                    if (interactiveObject != null) {
                        interactiveObject.entity = new AnimatedObject(interactiveObject.uid >> 14 & 0x7fff, objectRot, objectType, i19, l19, j18, k20, animationId, false);
                    }
                }
                if (j16 == 3) {
                    GroundDecoration groundDecoration = sceneGraph.getGroundDecoration(y, x, plane);
                    if (groundDecoration != null) {
                        groundDecoration.entity = new AnimatedObject(groundDecoration.uid >> 14 & 0x7fff, objectRot, 22, i19, l19, j18, k20, animationId, false);
                    }
                }
            }
            return;
        }
        if (opcode == 147) {
            int loc = packetBuffer.getUnsignedByteS();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            int playerIndex = packetBuffer.getUnsignedShort();
            byte byte0 = packetBuffer.getByteS();
            int l14 = packetBuffer.getUnsignedLEShort();
            byte byte1 = packetBuffer.getByteC();
            int k17 = packetBuffer.getUnsignedShort();
            int objectData = packetBuffer.getUnsignedByteS();
            int objectType = objectData >> 2;
            int objectRot = objectData & 3;
            int l20 = anIntArray1177[objectType];
            byte byte2 = packetBuffer.getByte();
            int objectId = packetBuffer.getUnsignedShort();
            byte byte3 = packetBuffer.getByteC();
            Player player;
            if (playerIndex == unknownInt10) {
                player = Client.myPlayer;
            } else {
                player = localPlayers[playerIndex];
            }
            if (player != null) {
                ObjectDefinition object = ObjectDefinition.forId(objectId);
                int i22 = intGroundArray[plane][x][y];
                int j22 = intGroundArray[plane][x + 1][y];
                int k22 = intGroundArray[plane][x + 1][y + 1];
                int l22 = intGroundArray[plane][x][y + 1];
                Model model = object.method578(objectType, objectRot, i22, j22, k22, l22, -1);
                if (model != null) {
                    method130(k17 + 1, -1, 0, l20, y, 0, plane, x, l14 + 1);
                    player.anInt1707 = l14 + Client.loopCycle;
                    player.anInt1708 = k17 + Client.loopCycle;
                    player.aModel_1714 = model;
                    int length = object.ySize;
                    int width = object.xSize;
                    if (objectRot == 1 || objectRot == 3) {
                        length = object.xSize;
                        width = object.ySize;
                    }
                    player.anInt1711 = x * 128 + length * 64;
                    player.anInt1713 = y * 128 + width * 64;
                    player.anInt1712 = method42(plane, player.anInt1713, player.anInt1711);
                    if (byte2 > byte0) {
                        byte byte4 = byte2;
                        byte2 = byte0;
                        byte0 = byte4;
                    }
                    if (byte3 > byte1) {
                        byte byte5 = byte3;
                        byte3 = byte1;
                        byte1 = byte5;
                    }
                    player.anInt1719 = x + byte2;
                    player.anInt1721 = x + byte0;
                    player.anInt1720 = y + byte3;
                    player.anInt1722 = y + byte1;
                }
            }
        }
        if (opcode == 151) {
            int loc = packetBuffer.getUnsignedByteA();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            System.out.println(localX + "," + localY);
            int objectId = packetBuffer.getUnsignedLEShort();
            int objectData = packetBuffer.getUnsignedByteS();
            int objectType = objectData >> 2;
            int objectRot = objectData & 3;
            int l17 = anIntArray1177[objectType];
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                method130(-1, objectId, objectRot, l17, y, objectType, plane, x, 0);
            }
            return;
        }
        if (opcode == 4) {
            int loc = packetBuffer.getUnsignedByte();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            int spotAnimId = packetBuffer.getUnsignedShort();
            int height = packetBuffer.getUnsignedByte();
            int delay = packetBuffer.getUnsignedShort();
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                x = x * 128 + 64;
                y = y * 128 + 64;
                StillGraphic stillGraphic = new StillGraphic(plane, Client.loopCycle, delay, spotAnimId, method42(plane, y, x) - height, y, x);
                stillGraphicList.insertBack(stillGraphic);
            }
            return;
        }
        if (opcode == 44) {
            int itemId = packetBuffer.getUnsignedShortA();
            int itemAmount = packetBuffer.getUnsignedShort();
            int loc = packetBuffer.getUnsignedByte();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                GroundItem groundItem = new GroundItem();
                groundItem.id = itemId;
                groundItem.amount = itemAmount;
                if (groundItems[plane][x][y] == null) {
                    groundItems[plane][x][y] = new Deque();
                }
                groundItems[plane][x][y].insertBack(groundItem);
                spawnGroundItem(x, y);
            }
            return;
        }
        if (opcode == 101) {
            int objectData = packetBuffer.getUnsignedByteC();
            int objectType = objectData >> 2;
            int objectRot = objectData & 3;
            int i11 = anIntArray1177[objectType];
            int loc = packetBuffer.getUnsignedByte();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            System.out.println(localX + "," + localY);
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                method130(-1, -1, objectRot, i11, y, objectType, plane, x, 0);
            }
            return;
        }
        if (opcode == 117) {
            int loc = packetBuffer.getUnsignedByte();
            int x = localX + (loc >> 4 & 7);
            int y = localY + (loc & 7);
            int destinedX = x + packetBuffer.getByte();
            int destinedY = y + packetBuffer.getByte();
            int target = packetBuffer.getShort();
            int graphicId = packetBuffer.getUnsignedShort();
            int startHeight = packetBuffer.getUnsignedByte() * 4;
            int endHeight = packetBuffer.getUnsignedByte() * 4;
            int time = packetBuffer.getUnsignedShort();
            int speed = packetBuffer.getUnsignedShort();
            int slope = packetBuffer.getUnsignedByte();
            int angle = packetBuffer.getUnsignedByte();
            if (x >= 0 && y >= 0 && x < 104 && y < 104 && destinedX >= 0 && destinedY >= 0 && destinedX < 104 && destinedY < 104 && graphicId != 65535) {
                x = x * 128 + 64;
                y = y * 128 + 64;
                destinedX = destinedX * 128 + 64;
                destinedY = destinedY * 128 + 64;
                Projectile projectile = new Projectile(slope, endHeight, time + Client.loopCycle, speed + Client.loopCycle, angle, plane, method42(plane, y, x) - startHeight, y, x, target, graphicId);
                projectile.method455(time + Client.loopCycle, destinedY, method42(plane, destinedY, destinedX) - endHeight, destinedX);
                projectileList.insertBack(projectile);
            }
        }
    }

    private static void setLowMem() {
        SceneGraph.lowMem = true;
        Rasterizer.lowMem = true;
        Client.lowMem = true;
        Region.lowMem = true;
        ObjectDefinition.lowMem = true;
    }

    private void updateNpcMovement(Stream updateBuffer) { // method139
        updateBuffer.beginBitBlock();
        int count = updateBuffer.getBits(8);
        if (count < localNpcCount) {
            for (int l = count; l < localNpcCount; l++) {
                localPlayerIndices[localEntityCount++] = localNpcIndices[l];
            }
        }
        if (count > localNpcCount) {
            Signlink.reporterror(myUsername + " Too many npcs");
            throw new RuntimeException("eek");
        }
        localNpcCount = 0;
        for (int id = 0; id < count; id++) {
            int localNpcId = localNpcIndices[id];
            Npc npc = localNpcs[localNpcId];
            int flag = updateBuffer.getBits(1);
            if (flag == 0) {
                localNpcIndices[localNpcCount++] = localNpcId;
                npc.lastUpdate = Client.loopCycle;
            } else {
                int type = updateBuffer.getBits(2);
                if (type == 0) {
                    localNpcIndices[localNpcCount++] = localNpcId;
                    npc.lastUpdate = Client.loopCycle;
                    playerUpdateIndices[entityUpdateCount++] = localNpcId;
                } else if (type == 1) {
                    localNpcIndices[localNpcCount++] = localNpcId;
                    npc.lastUpdate = Client.loopCycle;
                    int i2 = updateBuffer.getBits(3);
                    npc.move(false, i2);
                    int k2 = updateBuffer.getBits(1);
                    if (k2 == 1) {
                        playerUpdateIndices[entityUpdateCount++] = localNpcId;
                    }
                } else if (type == 2) {
                    localNpcIndices[localNpcCount++] = localNpcId;
                    npc.lastUpdate = Client.loopCycle;
                    int j2 = updateBuffer.getBits(3);
                    npc.move(true, j2);
                    int l2 = updateBuffer.getBits(3);
                    npc.move(true, l2);
                    int i3 = updateBuffer.getBits(1);
                    if (i3 == 1) {
                        playerUpdateIndices[entityUpdateCount++] = localNpcId;
                    }
                } else if (type == 3) {
                    localPlayerIndices[localEntityCount++] = localNpcId;
                }
            }
        }
    }

    private void processLoginScreenInput() {
        if (loginScreenState == 0) {
            int x = super.width / 2 - 80;
            int y = super.height / 2 + 20;
            y += 20;
            if (super.clickMode3 == 1 && super.saveClickX >= x - 75 && super.saveClickX <= x + 75 && super.saveClickY >= y - 20 && super.saveClickY <= y + 20) {
                loginScreenState = 3;
                loginScreenCursorPos = 0;
            }
            x = super.width / 2 + 80;
            if (super.clickMode3 == 1 && super.saveClickX >= x - 75 && super.saveClickX <= x + 75 && super.saveClickY >= y - 20 && super.saveClickY <= y + 20) {
                loginMessage1 = "";
                loginMessage2 = "Enter your username & password.";
                loginScreenState = 2;
                loginScreenCursorPos = 0;
            }
        } else {
            if (loginScreenState == 2) {
                int y = super.height / 2 - 40;
                y += 30;
                y += 25;
                if (super.clickMode3 == 1 && super.saveClickY >= y - 15 && super.saveClickY < y) {
                    loginScreenCursorPos = 0;
                }
                y += 15;
                if (super.clickMode3 == 1 && super.saveClickY >= y - 15 && super.saveClickY < y) {
                    loginScreenCursorPos = 1;
                }
                y += 15;
                int x = super.width / 2 - 80;
                int y_ = super.height / 2 + 50;
                y_ += 20;
                if (super.clickMode3 == 1 && super.saveClickX >= x - 75 && super.saveClickX <= x + 75 && super.saveClickY >= y_ - 20 && super.saveClickY <= y_ + 20) {
                    loginFailures = 0;
                    login(myUsername, myPassword, false);
                    if (loggedIn) {
                        return;
                    }
                }
                x = super.width / 2 + 80;
                if (super.clickMode3 == 1 && super.saveClickX >= x - 75 && super.saveClickX <= x + 75 && super.saveClickY >= y_ - 20 && super.saveClickY <= y_ + 20) {
                    loginScreenState = 0;
                    // myUsername = "";
                    // myPassword = "";
                }
                do {
                    int nextKey = getNextChar();
                    if (nextKey == -1) {
                        break;
                    }
                    boolean flag1 = false;
                    for (int i2 = 0; i2 < Client.validUserPassChars.length(); i2++) {
                        if (nextKey != Client.validUserPassChars.charAt(i2)) {
                            continue;
                        }
                        flag1 = true;
                        break;
                    }
                    if (loginScreenCursorPos == 0) {
                        if (nextKey == 8 && myUsername.length() > 0) {
                            myUsername = myUsername.substring(0, myUsername.length() - 1);
                        }
                        if (nextKey == 9 || nextKey == 10 || nextKey == 13) {
                            loginScreenCursorPos = 1;
                        }
                        if (flag1) {
                            myUsername += (char) nextKey;
                        }
                        if (myUsername.length() > 12) {
                            myUsername = myUsername.substring(0, 12);
                        }
                    } else if (loginScreenCursorPos == 1) {
                        if (nextKey == 8 && myPassword.length() > 0) {
                            myPassword = myPassword.substring(0, myPassword.length() - 1);
                        }
                        if (nextKey == 9 || nextKey == 10 || nextKey == 13) {
                            loginScreenCursorPos = 0;
                        }
                        if (flag1) {
                            myPassword += (char) nextKey;
                        }
                        if (myPassword.length() > 20) {
                            myPassword = myPassword.substring(0, 20);
                        }
                    }
                } while (true);
                return;
            }
            if (loginScreenState == 3) {
                int k = super.width / 2;
                int j1 = super.height / 2 + 50;
                j1 += 20;
                if (super.clickMode3 == 1 && super.saveClickX >= k - 75 && super.saveClickX <= k + 75 && super.saveClickY >= j1 - 20 && super.saveClickY <= j1 + 20) {
                    loginScreenState = 0;
                }
            }
        }
    }

    private void markMinimap(Sprite sprite, int x, int y) {
        int k = cameraX + minimapInt2 & 0x7ff;
        int l = x * x + y * y;
        if (l > 6400) {
            return;
        }
        int i1 = Model.modelIntArray1[k];
        int j1 = Model.modelIntArray2[k];
        i1 = i1 * 256 / (minimapInt3 + 256);
        j1 = j1 * 256 / (minimapInt3 + 256);
        int k1 = y * i1 + x * j1 >> 16;
        int l1 = y * j1 - x * i1 >> 16;
        if (l > 2500) {
            sprite.method354(mapBack, 83 - l1 - sprite.trimHeight / 2 - 4, 94 + k1 - sprite.trimWidth / 2 + 4);
        } else {
            sprite.drawSprite(94 + k1 - sprite.trimWidth / 2 + 4, 83 - l1 - sprite.trimHeight / 2 - 4);
        }
    }

    private void method142(int y, int z, int arg2, int arg3, int x, int arg5, int arg6) {
        if (x >= 1 && y >= 1 && x <= 102 && y <= 102) {
            if (Client.lowMem && z != plane) {
                return;
            }
            int loc = 0;
            if (arg5 == 0) {
                loc = sceneGraph.getWallObjectUid(z, x, y);
            }
            if (arg5 == 1) {
                loc = sceneGraph.getWallDecorationUid(z, x, y);
            }
            if (arg5 == 2) {
                loc = sceneGraph.getInteractiveObjectUid(z, x, y);
            }
            if (arg5 == 3) {
                loc = sceneGraph.getGroundDecorationUid(z, x, y);
            }
            if (loc != 0) {
                int i3 = sceneGraph.method304(z, x, y, loc);
                int j2 = loc >> 14 & 0x7fff;
                int k2 = i3 & 0x1f;
                int l2 = i3 >> 6;
                if (arg5 == 0) {
                    sceneGraph.resetWallObject(x, z, y);
                    ObjectDefinition object = ObjectDefinition.forId(j2);
                    if (object.unwalkable) {
                        collisionMaps[z].method215(l2, k2, object.solid, x, y);
                    }
                }
                if (arg5 == 1) {
                    sceneGraph.resetWallDecoration(y, z, x);
                }
                if (arg5 == 2) {
                    sceneGraph.method293(z, x, y);
                    ObjectDefinition object = ObjectDefinition.forId(j2);
                    if (x + object.ySize > 103 || y + object.ySize > 103 || x + object.xSize > 103 || y + object.xSize > 103) {
                        return;
                    }
                    if (object.unwalkable) {
                        collisionMaps[z].method216(l2, object.ySize, x, y, object.xSize, object.solid);
                    }
                }
                if (arg5 == 3) {
                    sceneGraph.resetGroundDecoration(z, y, x);
                    ObjectDefinition objectDefinition_2 = ObjectDefinition.forId(j2);
                    if (objectDefinition_2.unwalkable && objectDefinition_2.hasActions) {
                        collisionMaps[z].method218(y, x);
                    }
                }
            }
            if (arg6 >= 0) {
                int j3 = z;
                if (j3 < 3 && (byteGroundArray[1][x][y] & 2) == 2) {
                    j3++;
                }
                Region.method188(sceneGraph, arg2, y, arg3, j3, collisionMaps[z], intGroundArray, x, arg6, z);
            }
        }
    }

    private void updatePlayers(int packetSize, Stream stream) {
        localEntityCount = 0;
        entityUpdateCount = 0;
        updateLocalPlayerMovement(stream);
        updatePlayerMovement(stream);
        updatePlayerList(stream, packetSize); // method91
        parsePlayerUpdateFlags(stream);
        for (int id = 0; id < localEntityCount; id++) {
            int localPlayerId = localPlayerIndices[id];
            if (localPlayers[localPlayerId].lastUpdate != Client.loopCycle) {
                localPlayers[localPlayerId] = null;
            }
        }
        if (stream.offset != packetSize) {
            Signlink.reporterror("Error packet size mismatch in getplayer pos:" + stream.offset + " psize:" + packetSize);
            throw new RuntimeException("eek");
        }
        for (int id = 0; id < localPlayerCount; id++) {
            if (localPlayers[playerIndices[id]] == null) {
                Signlink.reporterror(myUsername + " null entry in pl list - pos:" + id + " size:" + localPlayerCount);
                throw new RuntimeException("eek");
            }
        }
    }

    private void setCameraPos(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
        int l1 = 2048 - arg1 & 0x7ff;
        int i2 = 2048 - arg4 & 0x7ff;
        int yaw = 0;
        int zoom = 0;
        int pitch = arg0;
        if (l1 != 0) {
            int i3 = Model.modelIntArray1[l1];
            int k3 = Model.modelIntArray2[l1];
            int i4 = zoom * k3 - pitch * i3 >> 16;
            pitch = zoom * i3 + pitch * k3 >> 16;
            zoom = i4;
        }
        if (i2 != 0) {
            int j3 = Model.modelIntArray1[i2];
            int l3 = Model.modelIntArray2[i2];
            int j4 = pitch * j3 + yaw * l3 >> 16;
            pitch = pitch * l3 - yaw * j3 >> 16;
            yaw = j4;
        }
        xCameraPos = arg2 - yaw;
        zCameraPos = arg3 - zoom;
        yCameraPos = arg5 - pitch;
        yCameraCurve = arg1;
        xCameraCurve = arg4;
    }

    private boolean parsePacket() {
        if (socketStream == null) {
            return false;
        }
        try {
            int i = socketStream.available();
            if (i == 0) {
                return false;
            }
            if (packetOpcode == -1) {
                socketStream.flushInputStream(inputStream.payload, 1);
                packetOpcode = inputStream.payload[0] & 0xff;
                if (encryption != null) {
                    packetOpcode = packetOpcode - encryption.getNextKey() & 0xff;
                }
                packetSize = PacketSizes.packetSizes[packetOpcode];
                i--;
            }
            if (packetSize == -1) {
                if (i > 0) {
                    socketStream.flushInputStream(inputStream.payload, 1);
                    packetSize = inputStream.payload[0] & 0xff;
                    i--;
                } else {
                    return false;
                }
            }
            if (packetSize == -2) {
                if (i > 1) {
                    socketStream.flushInputStream(inputStream.payload, 2);
                    inputStream.offset = 0;
                    packetSize = inputStream.getUnsignedShort();
                    i -= 2;
                } else {
                    return false;
                }
            }
            if (i < packetSize) {
                return false;
            }
            inputStream.offset = 0;
            socketStream.flushInputStream(inputStream.payload, packetSize);
            anInt1009 = 0;
            anInt843 = anInt842;
            anInt842 = anInt841;
            anInt841 = packetOpcode;
            if (packetOpcode == 81) {
                updatePlayers(packetSize, inputStream);
                aBoolean1080 = false;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 176) {
                daysSinceRecovChange = inputStream.getUnsignedByteC();
                unreadMessages = inputStream.getUnsignedLEShortA();
                membersInt = inputStream.getUnsignedByte();
                anInt1193 = inputStream.getInt1();
                daysSinceLastLogin = inputStream.getUnsignedShort();
                if (anInt1193 != 0 && openInterfaceId == -1) {
                    Signlink.dnslookup(TextUtil.decodeAddress(anInt1193));
                    closeOpenInterfaces();
                    char c = '\u028A';
                    if (daysSinceRecovChange != 201 || membersInt == 1) {
                        c = '\u028F';
                    }
                    reportAbuseInput = "";
                    canMute = false;
                    for (Interface element : Interface.cachedInterfaces) {
                        if (element == null || element.contentType != c) {
                            continue;
                        }
                        openInterfaceId = element.parentId;
                        break;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 64) {
                localX = inputStream.getUnsignedByteC();
                localY = inputStream.getUnsignedByteS();
                for (int j = localX; j < localX + 8; j++) {
                    for (int l9 = localY; l9 < localY + 8; l9++) {
                        if (groundItems[plane][j][l9] != null) {
                            groundItems[plane][j][l9] = null;
                            spawnGroundItem(j, l9);
                        }
                    }
                }
                for (SceneObject object = (SceneObject) aClass19_1179.getFront(); object != null; object = (SceneObject) aClass19_1179.getNext()) {
                    if (object.anInt1297 >= localX && object.anInt1297 < localX + 8 && object.anInt1298 >= localY && object.anInt1298 < localY + 8 && object.anInt1295 == plane) {
                        object.anInt1294 = 0;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 185) {
                int k = inputStream.getUnsignedShortA();
                Interface.cachedInterfaces[k].anInt233 = 3;
                if (Client.myPlayer.desc == null) {
                    Interface.cachedInterfaces[k].mediaId = (Client.myPlayer.anIntArray1700[0] << 25) + (Client.myPlayer.anIntArray1700[4] << 20) + (Client.myPlayer.equipmentIds[0] << 15) + (Client.myPlayer.equipmentIds[8] << 10) + (Client.myPlayer.equipmentIds[11] << 5) + Client.myPlayer.equipmentIds[1];
                } else {
                    Interface.cachedInterfaces[k].mediaId = (int) (0x12345678L + Client.myPlayer.desc.type);
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 107) {
                aBoolean1160 = false;
                for (int l = 0; l < 5; l++) {
                    aBooleanArray876[l] = false;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 72) {
                int i1 = inputStream.getUnsignedLEShort();
                Interface component = Interface.cachedInterfaces[i1];
                for (int k15 = 0; k15 < component.inv.length; k15++) {
                    component.inv[k15] = -1;
                    component.inv[k15] = 0;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 214) {
                ignoreCount = packetSize / 8;
                for (int j1 = 0; j1 < ignoreCount; j1++) {
                    ignoreListAsLongs[j1] = inputStream.getLong();
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 166) {
                aBoolean1160 = true;
                anInt1098 = inputStream.getUnsignedByte();
                anInt1099 = inputStream.getUnsignedByte();
                anInt1100 = inputStream.getUnsignedShort();
                anInt1101 = inputStream.getUnsignedByte();
                anInt1102 = inputStream.getUnsignedByte();
                if (anInt1102 >= 100) {
                    xCameraPos = anInt1098 * 128 + 64;
                    yCameraPos = anInt1099 * 128 + 64;
                    zCameraPos = method42(plane, yCameraPos, xCameraPos) - anInt1100;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 134) {
                needDrawTabArea = true;
                int k1 = inputStream.getUnsignedByte();
                int i10 = inputStream.getInt2();
                int l15 = inputStream.getUnsignedByte();
                currentExp[k1] = i10;
                currentStats[k1] = l15;
                maxStats[k1] = 1;
                for (int k20 = 0; k20 < 98; k20++) {
                    if (i10 >= Client.anIntArray1019[k20]) {
                        maxStats[k1] = k20 + 2;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 71) {
                int l1 = inputStream.getUnsignedShort();
                int j10 = inputStream.getUnsignedByteA();
                if (l1 == 65535) {
                    l1 = -1;
                }
                tabInterfaceIDs[j10] = l1;
                needDrawTabArea = true;
                tabAreaAltered = true;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 74) {
                int i2 = inputStream.getUnsignedLEShort();
                if (i2 == 65535) {
                    i2 = -1;
                }
                if (i2 != currentSong && musicEnabled && !Client.lowMem && prevSong == 0) {
                    nextSong = i2;
                    songChanging = true;
                    onDemandFetcher.method558(2, nextSong);
                }
                currentSong = i2;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 121) {
                int j2 = inputStream.getUnsignedShortA();
                int k10 = inputStream.getUnsignedLEShortA();
                if (musicEnabled && !Client.lowMem) {
                    nextSong = j2;
                    songChanging = false;
                    onDemandFetcher.method558(2, nextSong);
                    prevSong = k10;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 109) {
                processLogout();
                packetOpcode = -1;
                return false;
            }
            if (packetOpcode == 70) {
                int k2 = inputStream.getShort();
                int l10 = inputStream.getLEShort();
                int i16 = inputStream.getUnsignedLEShort();
                Interface component = Interface.cachedInterfaces[i16];
                component.anInt263 = k2;
                component.anInt265 = l10;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 73 || packetOpcode == 241) {
                // mapReset();
                int l2 = anInt1069;
                int i11 = anInt1070;
                if (packetOpcode == 73) {
                    l2 = inputStream.getUnsignedLEShortA();
                    i11 = inputStream.getUnsignedShort();
                    aBoolean1159 = false;
                }
                if (packetOpcode == 241) {
                    i11 = inputStream.getUnsignedLEShortA();
                    inputStream.beginBitBlock();
                    for (int j16 = 0; j16 < 4; j16++) {
                        for (int l20 = 0; l20 < 13; l20++) {
                            for (int j23 = 0; j23 < 13; j23++) {
                                int i26 = inputStream.getBits(1);
                                if (i26 == 1) {
                                    anIntArrayArrayArray1129[j16][l20][j23] = inputStream.getBits(26);
                                } else {
                                    anIntArrayArrayArray1129[j16][l20][j23] = -1;
                                }
                            }
                        }
                    }
                    inputStream.endBitBlock();
                    l2 = inputStream.getUnsignedShort();
                    aBoolean1159 = true;
                }
                if (anInt1069 == l2 && anInt1070 == i11 && loadingStage == 2) {
                    packetOpcode = -1;
                    return true;
                }
                anInt1069 = l2;
                anInt1070 = i11;
                baseX = (anInt1069 - 6) * 8;
                baseY = (anInt1070 - 6) * 8;
                aBoolean1141 = (anInt1069 / 8 == 48 || anInt1069 / 8 == 49) && anInt1070 / 8 == 48;
                if (anInt1069 / 8 == 48 && anInt1070 / 8 == 148) {
                    aBoolean1141 = true;
                }
                loadingStage = 1;
                aLong824 = System.currentTimeMillis();
                gameScreenCanvas.initDrawingArea();
                aFont_1271.drawANCText(0, "Loading - please wait.", 151, 257);
                aFont_1271.drawANCText(0xffffff, "Loading - please wait.", 150, 256);
                gameScreenCanvas.drawGraphics(4, super.graphics, 4);
                if (packetOpcode == 73) {
                    int k16 = 0;
                    for (int i21 = (anInt1069 - 6) / 8; i21 <= (anInt1069 + 6) / 8; i21++) {
                        for (int k23 = (anInt1070 - 6) / 8; k23 <= (anInt1070 + 6) / 8; k23++) {
                            k16++;
                        }
                    }
                    aByteArrayArray1183 = new byte[k16][];
                    aByteArrayArray1247 = new byte[k16][];
                    anIntArray1234 = new int[k16];
                    anIntArray1235 = new int[k16];
                    anIntArray1236 = new int[k16];
                    k16 = 0;
                    for (int l23 = (anInt1069 - 6) / 8; l23 <= (anInt1069 + 6) / 8; l23++) {
                        for (int j26 = (anInt1070 - 6) / 8; j26 <= (anInt1070 + 6) / 8; j26++) {
                            anIntArray1234[k16] = (l23 << 8) + j26;
                            if (aBoolean1141 && (j26 == 49 || j26 == 149 || j26 == 147 || l23 == 50 || l23 == 49 && j26 == 47)) {
                                anIntArray1235[k16] = -1;
                                anIntArray1236[k16] = -1;
                                k16++;
                            } else {
                                int k28 = anIntArray1235[k16] = onDemandFetcher.getMapCount(0, j26, l23);
                                if (k28 != -1) {
                                    onDemandFetcher.method558(3, k28);
                                }
                                int j30 = anIntArray1236[k16] = onDemandFetcher.getMapCount(1, j26, l23);
                                if (j30 != -1) {
                                    onDemandFetcher.method558(3, j30);
                                }
                                k16++;
                            }
                        }
                    }
                }
                if (packetOpcode == 241) {
                    int l16 = 0;
                    int ai[] = new int[676];
                    for (int i24 = 0; i24 < 4; i24++) {
                        for (int k26 = 0; k26 < 13; k26++) {
                            for (int l28 = 0; l28 < 13; l28++) {
                                int k30 = anIntArrayArrayArray1129[i24][k26][l28];
                                if (k30 != -1) {
                                    int k31 = k30 >> 14 & 0x3ff;
                                    int i32 = k30 >> 3 & 0x7ff;
                                    int k32 = (k31 / 8 << 8) + i32 / 8;
                                    for (int j33 = 0; j33 < l16; j33++) {
                                        if (ai[j33] != k32) {
                                            continue;
                                        }
                                        k32 = -1;
                                        break;
                                    }
                                    if (k32 != -1) {
                                        ai[l16++] = k32;
                                    }
                                }
                            }
                        }
                    }
                    aByteArrayArray1183 = new byte[l16][];
                    aByteArrayArray1247 = new byte[l16][];
                    anIntArray1234 = new int[l16];
                    anIntArray1235 = new int[l16];
                    anIntArray1236 = new int[l16];
                    for (int l26 = 0; l26 < l16; l26++) {
                        int i29 = anIntArray1234[l26] = ai[l26];
                        int l30 = i29 >> 8 & 0xff;
                        int l31 = i29 & 0xff;
                        int j32 = anIntArray1235[l26] = onDemandFetcher.getMapCount(0, l31, l30);
                        if (j32 != -1) {
                            onDemandFetcher.method558(3, j32);
                        }
                        int i33 = anIntArray1236[l26] = onDemandFetcher.getMapCount(1, l31, l30);
                        if (i33 != -1) {
                            onDemandFetcher.method558(3, i33);
                        }
                    }
                }
                int i17 = baseX - anInt1036;
                int j21 = baseY - anInt1037;
                anInt1036 = baseX;
                anInt1037 = baseY;
                for (int j24 = 0; j24 < 16384; j24++) {
                    Npc npc = localNpcs[j24];
                    if (npc != null) {
                        for (int j29 = 0; j29 < 10; j29++) {
                            npc.walkQueueX[j29] -= i17;
                            npc.walkQueueY[j29] -= j21;
                        }
                        npc.x -= i17 * 128;
                        npc.y -= j21 * 128;
                    }
                }
                for (int i27 = 0; i27 < maxPlayers; i27++) {
                    Player player = localPlayers[i27];
                    if (player != null) {
                        for (int i31 = 0; i31 < 10; i31++) {
                            player.walkQueueX[i31] -= i17;
                            player.walkQueueY[i31] -= j21;
                        }
                        player.x -= i17 * 128;
                        player.y -= j21 * 128;
                    }
                }
                aBoolean1080 = true;
                byte byte1 = 0;
                byte byte2 = 104;
                byte byte3 = 1;
                if (i17 < 0) {
                    byte1 = 103;
                    byte2 = -1;
                    byte3 = -1;
                }
                byte byte4 = 0;
                byte byte5 = 104;
                byte byte6 = 1;
                if (j21 < 0) {
                    byte4 = 103;
                    byte5 = -1;
                    byte6 = -1;
                }
                for (int k33 = byte1; k33 != byte2; k33 += byte3) {
                    for (int l33 = byte4; l33 != byte5; l33 += byte6) {
                        int i34 = k33 + i17;
                        int j34 = l33 + j21;
                        for (int k34 = 0; k34 < 4; k34++) {
                            if (i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104) {
                                groundItems[k34][k33][l33] = groundItems[k34][i34][j34];
                            } else {
                                groundItems[k34][k33][l33] = null;
                            }
                        }
                    }
                }
                for (SceneObject object = (SceneObject) aClass19_1179.getFront(); object != null; object = (SceneObject) aClass19_1179.getNext()) {
                    object.anInt1297 -= i17;
                    object.anInt1298 -= j21;
                    if (object.anInt1297 < 0 || object.anInt1298 < 0 || object.anInt1297 >= 104 || object.anInt1298 >= 104) {
                        object.unlink();
                    }
                }
                if (destX != 0) {
                    destX -= i17;
                    destY -= j21;
                }
                aBoolean1160 = false;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 208) {
                int i3 = inputStream.getLEShort();
                if (i3 >= 0) {
                    method60(i3);
                }
                anInt1018 = i3;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 99) {
                anInt1021 = inputStream.getUnsignedByte();
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 75) {
                int j3 = inputStream.getUnsignedShortA();
                int j11 = inputStream.getUnsignedShortA();
                Interface.cachedInterfaces[j11].anInt233 = 2;
                Interface.cachedInterfaces[j11].mediaId = j3;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 114) {
                systemUpdateTime = inputStream.getUnsignedLEShort() * 30;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 60) {
                localY = inputStream.getUnsignedByte();
                localX = inputStream.getUnsignedByteC();
                while (inputStream.offset < packetSize) {
                    int k3 = inputStream.getUnsignedByte();
                    method137(inputStream, k3);
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 35) {
                int l3 = inputStream.getUnsignedByte();
                int k11 = inputStream.getUnsignedByte();
                int j17 = inputStream.getUnsignedByte();
                int k21 = inputStream.getUnsignedByte();
                aBooleanArray876[l3] = true;
                anIntArray873[l3] = k11;
                anIntArray1203[l3] = j17;
                anIntArray928[l3] = k21;
                anIntArray1030[l3] = 0;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 174) {
                int i4 = inputStream.getUnsignedShort();
                int l11 = inputStream.getUnsignedByte();
                int k17 = inputStream.getUnsignedShort();
                if (aBoolean848 && !Client.lowMem && anInt1062 < 50) {
                    anIntArray1207[anInt1062] = i4;
                    anIntArray1241[anInt1062] = l11;
                    anIntArray1250[anInt1062] = k17 + Sound.anIntArray326[i4];
                    anInt1062++;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 104) {
                int j4 = inputStream.getUnsignedByteC();
                int i12 = inputStream.getUnsignedByteA();
                String s6 = inputStream.getString();
                if (j4 >= 1 && j4 <= 5) {
                    if (s6.equalsIgnoreCase("null")) {
                        s6 = null;
                    }
                    atPlayerActions[j4 - 1] = s6;
                    atPlayerArray[j4 - 1] = i12 == 0;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 78) {
                destX = 0;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 253) {
                String s = inputStream.getString();
                if (s.endsWith(":tradereq:")) {
                    String s3 = s.substring(0, s.indexOf(":"));
                    long l17 = TextUtil.nameToLong(s3);
                    boolean flag2 = false;
                    for (int j27 = 0; j27 < ignoreCount; j27++) {
                        if (ignoreListAsLongs[j27] != l17) {
                            continue;
                        }
                        flag2 = true;
                        break;
                    }
                    if (!flag2 && anInt1251 == 0) {
                        pushMessage("wishes to trade with you.", 4, s3);
                    }
                } else if (s.endsWith(":duelreq:")) {
                    String s4 = s.substring(0, s.indexOf(":"));
                    long l18 = TextUtil.nameToLong(s4);
                    boolean flag3 = false;
                    for (int k27 = 0; k27 < ignoreCount; k27++) {
                        if (ignoreListAsLongs[k27] != l18) {
                            continue;
                        }
                        flag3 = true;
                        break;
                    }
                    if (!flag3 && anInt1251 == 0) {
                        pushMessage("wishes to duel with you.", 8, s4);
                    }
                } else if (s.endsWith(":chalreq:")) {
                    String s5 = s.substring(0, s.indexOf(":"));
                    long l19 = TextUtil.nameToLong(s5);
                    boolean flag4 = false;
                    for (int l27 = 0; l27 < ignoreCount; l27++) {
                        if (ignoreListAsLongs[l27] != l19) {
                            continue;
                        }
                        flag4 = true;
                        break;
                    }
                    if (!flag4 && anInt1251 == 0) {
                        String s8 = s.substring(s.indexOf(":") + 1, s.length() - 9);
                        pushMessage(s8, 8, s5);
                    }
                } else {
                    pushMessage(s, 0, "");
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 1) {
                for (int k4 = 0; k4 < localPlayers.length; k4++) {
                    if (localPlayers[k4] != null) {
                        localPlayers[k4].animationId = -1;
                    }
                }
                for (int j12 = 0; j12 < localNpcs.length; j12++) {
                    if (localNpcs[j12] != null) {
                        localNpcs[j12].animationId = -1;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 50) {
                long l4 = inputStream.getLong();
                int i18 = inputStream.getUnsignedByte();
                String s7 = TextUtil.formatName(TextUtil.longToName(l4));
                for (int k24 = 0; k24 < friendsCount; k24++) {
                    if (l4 != friendsNamesAsLongs[k24]) {
                        continue;
                    }
                    if (friendsWorlds[k24] != i18) {
                        friendsWorlds[k24] = i18;
                        needDrawTabArea = true;
                        if (i18 > 0) {
                            pushMessage(s7 + " has logged in.", 5, "");
                        }
                        if (i18 == 0) {
                            pushMessage(s7 + " has logged out.", 5, "");
                        }
                    }
                    s7 = null;
                    break;
                }
                if (s7 != null && friendsCount < 200) {
                    friendsNamesAsLongs[friendsCount] = l4;
                    friendsNames[friendsCount] = s7;
                    friendsWorlds[friendsCount] = i18;
                    friendsCount++;
                    needDrawTabArea = true;
                }
                for (boolean flag6 = false; !flag6;) {
                    flag6 = true;
                    for (int k29 = 0; k29 < friendsCount - 1; k29++) {
                        if (friendsWorlds[k29] != Client.nodeID && friendsWorlds[k29 + 1] == Client.nodeID || friendsWorlds[k29] == 0 && friendsWorlds[k29 + 1] != 0) {
                            int j31 = friendsWorlds[k29];
                            friendsWorlds[k29] = friendsWorlds[k29 + 1];
                            friendsWorlds[k29 + 1] = j31;
                            String s10 = friendsNames[k29];
                            friendsNames[k29] = friendsNames[k29 + 1];
                            friendsNames[k29 + 1] = s10;
                            long l32 = friendsNamesAsLongs[k29];
                            friendsNamesAsLongs[k29] = friendsNamesAsLongs[k29 + 1];
                            friendsNamesAsLongs[k29 + 1] = l32;
                            needDrawTabArea = true;
                            flag6 = false;
                        }
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 110) {
                if (tabID == 12) {
                    needDrawTabArea = true;
                }
                energy = inputStream.getUnsignedByte();
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 254) {
                anInt855 = inputStream.getUnsignedByte();
                if (anInt855 == 1) {
                    anInt1222 = inputStream.getUnsignedShort();
                }
                if (anInt855 >= 2 && anInt855 <= 6) {
                    if (anInt855 == 2) {
                        anInt937 = 64;
                        anInt938 = 64;
                    }
                    if (anInt855 == 3) {
                        anInt937 = 0;
                        anInt938 = 64;
                    }
                    if (anInt855 == 4) {
                        anInt937 = 128;
                        anInt938 = 64;
                    }
                    if (anInt855 == 5) {
                        anInt937 = 64;
                        anInt938 = 0;
                    }
                    if (anInt855 == 6) {
                        anInt937 = 64;
                        anInt938 = 128;
                    }
                    anInt855 = 2;
                    anInt934 = inputStream.getUnsignedShort();
                    anInt935 = inputStream.getUnsignedShort();
                    anInt936 = inputStream.getUnsignedByte();
                }
                if (anInt855 == 10) {
                    anInt933 = inputStream.getUnsignedShort();
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 248) {
                int i5 = inputStream.getUnsignedLEShortA();
                int k12 = inputStream.getUnsignedShort();
                if (backDialogID != -1) {
                    backDialogID = -1;
                    inputTaken = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    inputTaken = true;
                }
                openInterfaceId = i5;
                invOverlayInterfaceID = k12;
                needDrawTabArea = true;
                tabAreaAltered = true;
                aBoolean1149 = false;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 79) {
                int j5 = inputStream.getUnsignedLEShort();
                int l12 = inputStream.getUnsignedLEShortA();
                Interface component = Interface.cachedInterfaces[j5];
                if (component != null && component.interfaceType == 0) {
                    if (l12 < 0) {
                        l12 = 0;
                    }
                    if (l12 > component.scrollMax - component.height) {
                        l12 = component.scrollMax - component.height;
                    }
                    component.scrollPosition = l12;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 68) {
                for (int k5 = 0; k5 < configStates.length; k5++) {
                    if (configStates[k5] != anIntArray1045[k5]) {
                        configStates[k5] = anIntArray1045[k5];
                        method33(k5);
                        needDrawTabArea = true;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 196) {
                long privateMessageFromPlayerAsLong = inputStream.getLong();
                int j18 = inputStream.getInt();
                int privateMessageFromPlayersRights = inputStream.getUnsignedByte();
                boolean flag5 = false;
                for (int i28 = 0; i28 < 100; i28++) {
                    if (anIntArray1240[i28] != j18) {
                        continue;
                    }
                    flag5 = true;
                    break;
                }
                if (privateMessageFromPlayersRights <= 1) {
                    for (int l29 = 0; l29 < ignoreCount; l29++) {
                        if (ignoreListAsLongs[l29] != privateMessageFromPlayerAsLong) {
                            continue;
                        }
                        flag5 = true;
                        break;
                    }
                }
                if (!flag5 && anInt1251 == 0) {
                    try {
                        anIntArray1240[anInt1169] = j18;
                        anInt1169 = (anInt1169 + 1) % 100;
                        String privateMessage = PlayerInput.unpackMessage(packetSize - 13, inputStream);
                        if (privateMessageFromPlayersRights != 3) {
                            privateMessage = Censor.doCensor(privateMessage);
                        }
                        if (privateMessageFromPlayersRights == 2 || privateMessageFromPlayersRights == 3) {
                            pushMessage(privateMessage, 7, "@cr2@" + TextUtil.formatName(TextUtil.longToName(privateMessageFromPlayerAsLong)));
                        } else if (privateMessageFromPlayersRights == 1) {
                            pushMessage(privateMessage, 7, "@cr1@" + TextUtil.formatName(TextUtil.longToName(privateMessageFromPlayerAsLong)));
                        } else {
                            pushMessage(privateMessage, 3, TextUtil.formatName(TextUtil.longToName(privateMessageFromPlayerAsLong)));
                        }
                    } catch (Exception exception1) {
                        Signlink.reporterror("cde1");
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 85) {
                localY = inputStream.getUnsignedByteC();
                localX = inputStream.getUnsignedByteC();
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 24) {
                anInt1054 = inputStream.getUnsignedByteS();
                if (anInt1054 == tabID) {
                    if (anInt1054 == 3) {
                        tabID = 1;
                    } else {
                        tabID = 3;
                    }
                    needDrawTabArea = true;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 246) {
                int i6 = inputStream.getUnsignedLEShort();
                int i13 = inputStream.getUnsignedShort();
                int k18 = inputStream.getUnsignedShort();
                if (k18 == 65535) {
                    Interface.cachedInterfaces[i6].anInt233 = 0;
                    packetOpcode = -1;
                    return true;
                } else {
                    ItemDefinition itemDefinition = ItemDefinition.forId(k18);
                    Interface.cachedInterfaces[i6].anInt233 = 4;
                    Interface.cachedInterfaces[i6].mediaId = k18;
                    Interface.cachedInterfaces[i6].anInt270 = itemDefinition.rotationY;
                    Interface.cachedInterfaces[i6].anInt271 = itemDefinition.rotationX;
                    Interface.cachedInterfaces[i6].anInt269 = itemDefinition.modelZoom * 100 / i13;
                    packetOpcode = -1;
                    return true;
                }
            }
            if (packetOpcode == 171) {
                boolean flag1 = inputStream.getUnsignedByte() == 1;
                int j13 = inputStream.getUnsignedShort();
                Interface.cachedInterfaces[j13].aBoolean266 = flag1;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 142) {
                int j6 = inputStream.getUnsignedLEShort();
                method60(j6);
                if (backDialogID != -1) {
                    backDialogID = -1;
                    inputTaken = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    inputTaken = true;
                }
                invOverlayInterfaceID = j6;
                needDrawTabArea = true;
                tabAreaAltered = true;
                openInterfaceId = -1;
                aBoolean1149 = false;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 126) {
                String s1 = inputStream.getString();
                int k13 = inputStream.getUnsignedLEShortA();
                Interface.cachedInterfaces[k13].message = s1;
                if (Interface.cachedInterfaces[k13].parentId == tabInterfaceIDs[tabID]) {
                    needDrawTabArea = true;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 206) {
                publicChatMode = inputStream.getUnsignedByte();
                privateChatMode = inputStream.getUnsignedByte();
                tradeMode = inputStream.getUnsignedByte();
                aBoolean1233 = true;
                inputTaken = true;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 240) {
                if (tabID == 12) {
                    needDrawTabArea = true;
                }
                weight = inputStream.getShort();
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 8) {
                int k6 = inputStream.getUnsignedShortA();
                int l13 = inputStream.getUnsignedShort();
                Interface.cachedInterfaces[k6].anInt233 = 1;
                Interface.cachedInterfaces[k6].mediaId = l13;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 122) {
                int l6 = inputStream.getUnsignedShortA();
                int i14 = inputStream.getUnsignedShortA();
                int i19 = i14 >> 10 & 0x1f;
                int i22 = i14 >> 5 & 0x1f;
                int l24 = i14 & 0x1f;
                Interface.cachedInterfaces[l6].textColor = (i19 << 19) + (i22 << 11) + (l24 << 3);
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 53) {
                needDrawTabArea = true;
                int i7 = inputStream.getUnsignedShort();
                Interface component = Interface.cachedInterfaces[i7];
                int j19 = inputStream.getUnsignedShort();
                for (int j22 = 0; j22 < j19; j22++) {
                    int i25 = inputStream.getUnsignedByte();
                    if (i25 == 255) {
                        i25 = inputStream.getInt1();
                    }
                    component.inv[j22] = inputStream.getUnsignedShortA();
                    component.invStackSizes[j22] = i25;
                }
                for (int j25 = j19; j25 < component.inv.length; j25++) {
                    component.inv[j25] = 0;
                    component.invStackSizes[j25] = 0;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 230) {
                int j7 = inputStream.getUnsignedLEShortA();
                int j14 = inputStream.getUnsignedShort();
                int k19 = inputStream.getUnsignedShort();
                int k22 = inputStream.getUnsignedShortA();
                Interface.cachedInterfaces[j14].anInt270 = k19;
                Interface.cachedInterfaces[j14].anInt271 = k22;
                Interface.cachedInterfaces[j14].anInt269 = j7;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 221) {
                anInt900 = inputStream.getUnsignedByte();
                needDrawTabArea = true;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 177) {
                aBoolean1160 = true;
                anInt995 = inputStream.getUnsignedByte();
                anInt996 = inputStream.getUnsignedByte();
                anInt997 = inputStream.getUnsignedShort();
                anInt998 = inputStream.getUnsignedByte();
                anInt999 = inputStream.getUnsignedByte();
                if (anInt999 >= 100) {
                    int k7 = anInt995 * 128 + 64;
                    int k14 = anInt996 * 128 + 64;
                    int i20 = method42(plane, k14, k7) - anInt997;
                    int l22 = k7 - xCameraPos;
                    int k25 = i20 - zCameraPos;
                    int j28 = k14 - yCameraPos;
                    int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
                    yCameraCurve = (int) (Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
                    xCameraCurve = (int) (Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
                    if (yCameraCurve < 128) {
                        yCameraCurve = 128;
                    }
                    if (yCameraCurve > 383) {
                        yCameraCurve = 383;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 249) {
                anInt1046 = inputStream.getUnsignedByteA();
                unknownInt10 = inputStream.getUnsignedShortA();
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 65) {
                updateNPCs(inputStream, packetSize);
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 27) {
                messagePromptRaised = false;
                inputDialogState = 1;
                amountOrNameInput = "";
                inputTaken = true;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 187) {
                messagePromptRaised = false;
                inputDialogState = 2;
                amountOrNameInput = "";
                inputTaken = true;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 97) {
                int l7 = inputStream.getUnsignedShort();
                method60(l7);
                if (invOverlayInterfaceID != -1) {
                    invOverlayInterfaceID = -1;
                    needDrawTabArea = true;
                    tabAreaAltered = true;
                }
                if (backDialogID != -1) {
                    backDialogID = -1;
                    inputTaken = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    inputTaken = true;
                }
                openInterfaceId = l7;
                aBoolean1149 = false;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 218) {
                int i8 = inputStream.getLEShortA();
                dialogID = i8;
                inputTaken = true;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 87) {
                int j8 = inputStream.getUnsignedLEShort();
                int l14 = inputStream.getInt2();
                anIntArray1045[j8] = l14;
                if (configStates[j8] != l14) {
                    configStates[j8] = l14;
                    method33(j8);
                    needDrawTabArea = true;
                    if (dialogID != -1) {
                        inputTaken = true;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 36) {
                int k8 = inputStream.getUnsignedLEShort();
                byte byte0 = inputStream.getByte();
                anIntArray1045[k8] = byte0;
                if (configStates[k8] != byte0) {
                    configStates[k8] = byte0;
                    method33(k8);
                    needDrawTabArea = true;
                    if (dialogID != -1) {
                        inputTaken = true;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 61) {
                anInt1055 = inputStream.getUnsignedByte();
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 200) {
                int l8 = inputStream.getUnsignedShort();
                int i15 = inputStream.getShort();
                Interface component = Interface.cachedInterfaces[l8];
                component.anInt257 = i15;
                if (i15 == -1) {
                    component.anInt246 = 0;
                    component.anInt208 = 0;
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 219) {
                if (invOverlayInterfaceID != -1) {
                    invOverlayInterfaceID = -1;
                    needDrawTabArea = true;
                    tabAreaAltered = true;
                }
                if (backDialogID != -1) {
                    backDialogID = -1;
                    inputTaken = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    inputTaken = true;
                }
                openInterfaceId = -1;
                aBoolean1149 = false;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 34) {
                needDrawTabArea = true;
                int i9 = inputStream.getUnsignedShort();
                Interface component = Interface.cachedInterfaces[i9];
                while (inputStream.offset < packetSize) {
                    int j20 = inputStream.getSmart();
                    int i23 = inputStream.getUnsignedShort();
                    int l25 = inputStream.getUnsignedByte();
                    if (l25 == 255) {
                        l25 = inputStream.getInt();
                    }
                    if (j20 >= 0 && j20 < component.inv.length) {
                        component.inv[j20] = i23;
                        component.invStackSizes[j20] = l25;
                    }
                }
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 105 || packetOpcode == 84 || packetOpcode == 147 || packetOpcode == 215 || packetOpcode == 4 || packetOpcode == 117 || packetOpcode == 156 || packetOpcode == 44 || packetOpcode == 160 || packetOpcode == 101 || packetOpcode == 151) {
                method137(inputStream, packetOpcode);
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 106) {
                tabID = inputStream.getUnsignedByteC();
                needDrawTabArea = true;
                tabAreaAltered = true;
                packetOpcode = -1;
                return true;
            }
            if (packetOpcode == 164) {
                int j9 = inputStream.getUnsignedLEShort();
                method60(j9);
                if (invOverlayInterfaceID != -1) {
                    invOverlayInterfaceID = -1;
                    needDrawTabArea = true;
                    tabAreaAltered = true;
                }
                backDialogID = j9;
                inputTaken = true;
                openInterfaceId = -1;
                aBoolean1149 = false;
                packetOpcode = -1;
                return true;
            }
            Signlink.reporterror("T1 - " + packetOpcode + "," + packetSize + " - " + anInt842 + "," + anInt843);
            processLogout();
        } catch (IOException ioexception) {
            dropClient();
        } catch (Exception exception) {
            String s2 = "T2 - " + packetOpcode + "," + anInt842 + "," + anInt843 + " - " + packetSize + "," + (baseX + Client.myPlayer.walkQueueX[0]) + "," + (baseY + Client.myPlayer.walkQueueY[0]) + " - ";
            for (int j15 = 0; j15 < packetSize && j15 < 50; j15++) {
                s2 = s2 + inputStream.payload[j15] + ",";
            }
            Signlink.reporterror(s2);
            processLogout();
        }
        return true;
    }

    private void method146() {
        flashEffectCycle++;
        renderPlayers(true);
        renderNpcs(true);
        renderPlayers(false);
        renderNpcs(false);
        renderProjectiles();
        renderStillGraphics();
        if (!aBoolean1160) {
            int i = cameraY;
            if (anInt984 / 256 > i) {
                i = anInt984 / 256;
            }
            if (aBooleanArray876[4] && anIntArray1203[4] + 128 > i) {
                i = anIntArray1203[4] + 128;
            }
            int k = cameraX + anInt896 & 0x7ff;
            setCameraPos(600 + i * 3, i, anInt1014, method42(plane, Client.myPlayer.y, Client.myPlayer.x) - 50, k, anInt1015);
        }
        int j;
        if (!aBoolean1160) {
            j = method120();
        } else {
            j = method121();
        }
        int l = xCameraPos;
        int i1 = zCameraPos;
        int j1 = yCameraPos;
        int k1 = yCameraCurve;
        int l1 = xCameraCurve;
        for (int i2 = 0; i2 < 5; i2++) {
            if (aBooleanArray876[i2]) {
                int j2 = (int) (Math.random() * (double) (anIntArray873[i2] * 2 + 1) - (double) anIntArray873[i2] + Math.sin((double) anIntArray1030[i2] * (double) anIntArray928[i2] / 100D) * (double) anIntArray1203[i2]);
                if (i2 == 0) {
                    xCameraPos += j2;
                }
                if (i2 == 1) {
                    zCameraPos += j2;
                }
                if (i2 == 2) {
                    yCameraPos += j2;
                }
                if (i2 == 3) {
                    xCameraCurve = xCameraCurve + j2 & 0x7ff;
                }
                if (i2 == 4) {
                    yCameraCurve += j2;
                    if (yCameraCurve < 128) {
                        yCameraCurve = 128;
                    }
                    if (yCameraCurve > 383) {
                        yCameraCurve = 383;
                    }
                }
            }
        }
        int k2 = Rasterizer.texturePriority;
        Model.aBoolean1684 = true;
        Model.anInt1687 = 0;
        Model.anInt1685 = super.mouseX - 4;
        Model.anInt1686 = super.mouseY - 4;
        Graphics2D.resetPixels();
        sceneGraph.method313(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, j, yCameraCurve);
        sceneGraph.resetInteractiveObject();
        updateEntities();
        drawHeadIcon();
        method37(k2);
        draw3dScreen();
        gameScreenCanvas.drawGraphics(4, super.graphics, 4);
        xCameraPos = l;
        zCameraPos = i1;
        yCameraPos = j1;
        yCameraCurve = k1;
        xCameraCurve = l1;
    }

    private void closeOpenInterfaces() { // clearTopInterfaces
        outputStream.writeOpcode(130);
        if (invOverlayInterfaceID != -1) {
            invOverlayInterfaceID = -1;
            needDrawTabArea = true;
            aBoolean1149 = false;
            tabAreaAltered = true;
        }
        if (backDialogID != -1) {
            backDialogID = -1;
            inputTaken = true;
            aBoolean1149 = false;
        }
        openInterfaceId = -1;
    }

    private Client() {
        anIntArrayArray825 = new int[104][104];
        friendsWorlds = new int[200];
        groundItems = new Deque[4][104][104];
        aBoolean831 = false;
        aStream_834 = new Stream(new byte[5000]);
        localNpcs = new Npc[16384];
        localNpcIndices = new int[16384];
        localPlayerIndices = new int[1000];
        aStream_847 = Stream.allocate();
        aBoolean848 = true;
        openInterfaceId = -1;
        currentExp = new int[Skills.skillsCount];
        aBoolean872 = false;
        anIntArray873 = new int[5];
        anInt874 = -1;
        aBooleanArray876 = new boolean[5];
        drawFlames = false;
        reportAbuseInput = "";
        unknownInt10 = -1;
        menuOpen = false;
        inputString = "";
        maxPlayers = 2048;
        myPlayerIndex = 2047;
        localPlayers = new Player[maxPlayers];
        playerIndices = new int[maxPlayers];
        playerUpdateIndices = new int[maxPlayers];
        playerAppearanceBuffers = new Stream[maxPlayers];
        anInt897 = 1;
        anIntArrayArray901 = new int[104][104];
        anInt902 = 0x766654;
        aByteArray912 = new byte[16384];
        currentStats = new int[Skills.skillsCount];
        ignoreListAsLongs = new long[100];
        loadingError = false;
        anInt927 = 0x332d25;
        anIntArray928 = new int[5];
        anIntArrayArray929 = new int[104][104];
        chatTypes = new int[100];
        chatNames = new String[100];
        chatMessages = new String[100];
        sideIcons = new IndexedSprite[13];
        aBoolean954 = true;
        friendsNamesAsLongs = new long[200];
        currentSong = -1;
        drawingFlames = false;
        spriteDrawX = -1;
        spriteDrawY = -1;
        anIntArray968 = new int[33];
        anIntArray969 = new int[256];
        cacheIndices = new CacheFile[5];
        configStates = new int[2000];
        aBoolean972 = false;
        anInt975 = 50;
        anIntArray976 = new int[anInt975];
        anIntArray977 = new int[anInt975];
        anIntArray978 = new int[anInt975];
        anIntArray979 = new int[anInt975];
        anIntArray980 = new int[anInt975];
        anIntArray981 = new int[anInt975];
        anIntArray982 = new int[anInt975];
        aStringArray983 = new String[anInt975];
        anInt985 = -1;
        hitMarks = new Sprite[20];
        anIntArray990 = new int[5];
        aBoolean994 = false;
        anInt1002 = 0x23201b;
        amountOrNameInput = "";
        projectileList = new Deque();
        aBoolean1017 = false;
        anInt1018 = -1;
        anIntArray1030 = new int[5];
        aBoolean1031 = false;
        mapFunctions = new Sprite[100];
        dialogID = -1;
        maxStats = new int[Skills.skillsCount];
        anIntArray1045 = new int[2000];
        aBoolean1047 = true;
        anIntArray1052 = new int[151];
        anInt1054 = -1;
        stillGraphicList = new Deque();
        anIntArray1057 = new int[33];
        anInterface_1059 = new Interface();
        mapScenes = new IndexedSprite[100];
        anInt1063 = 0x4d4233;
        anIntArray1065 = new int[7];
        anIntArray1072 = new int[1000];
        anIntArray1073 = new int[1000];
        aBoolean1080 = false;
        friendsNames = new String[200];
        inputStream = Stream.allocate();
        expectedCRCs = new int[9];
        menuActionCmd2 = new int[500];
        menuActionCmd3 = new int[500];
        menuActionOpcode = new int[500];
        menuActionCmd1 = new int[500];
        hintIcon = new Sprite[32];
        prayerIcon = new Sprite[32];
        skullIcon = new Sprite[32];
        tabAreaAltered = false;
        aString1121 = "";
        atPlayerActions = new String[5];
        atPlayerArray = new boolean[5];
        anIntArrayArrayArray1129 = new int[4][13][13];
        anInt1132 = 2;
        aClass30_Sub2_Sub1_Sub1Array1140 = new Sprite[1000];
        aBoolean1141 = false;
        aBoolean1149 = false;
        crosses = new Sprite[8];
        musicEnabled = true;
        needDrawTabArea = false;
        loggedIn = false;
        canMute = false;
        aBoolean1159 = false;
        aBoolean1160 = false;
        anInt1171 = 1;
        myUsername = "mopar";
        myPassword = "bob";
        genericLoadingError = false;
        reportAbuseInterfaceID = -1;
        aClass19_1179 = new Deque();
        cameraY = 128;
        invOverlayInterfaceID = -1;
        outputStream = Stream.allocate();
        menuActionName = new String[500];
        anIntArray1203 = new int[5];
        anIntArray1207 = new int[50];
        anInt1210 = 2;
        anInt1211 = 78;
        promptInput = "";
        modIcons = new IndexedSprite[2];
        tabID = 3;
        inputTaken = false;
        songChanging = true;
        anIntArray1229 = new int[151];
        collisionMaps = new CollisionMap[4];
        aBoolean1233 = false;
        anIntArray1240 = new int[100];
        anIntArray1241 = new int[50];
        aBoolean1242 = false;
        anIntArray1250 = new int[50];
        alreadyLoaded = false;
        welcomeScreenRaised = false;
        messagePromptRaised = false;
        loginMessage1 = "";
        loginMessage2 = "";
        backDialogID = -1;
        anInt1279 = 2;
        bigX = new int[4000];
        bigY = new int[4000];
        anInt1289 = -1;
    }
    private int ignoreCount;
    private long aLong824;
    private int[][] anIntArrayArray825;
    private int[] friendsWorlds;
    private Deque[][][] groundItems;
    private int[] anIntArray828;
    private int[] anIntArray829;
    private volatile boolean aBoolean831;
    private java.net.Socket socket;
    private int loginScreenState;
    private Stream aStream_834;
    private Npc[] localNpcs;
    private int localNpcCount;
    private int[] localNpcIndices;
    private int localEntityCount;
    private int[] localPlayerIndices;
    private int anInt841;
    private int anInt842;
    private int anInt843;
    private String aString844;
    private int privateChatMode;
    private Stream aStream_847;
    private boolean aBoolean848;
    private static int anInt849;
    private int[] anIntArray850;
    private int[] anIntArray851;
    private int[] anIntArray852;
    private int[] anIntArray853;
    private static int anInt854;
    private int anInt855;
    private int openInterfaceId;
    private int xCameraPos;
    private int zCameraPos;
    private int yCameraPos;
    private int yCameraCurve;
    private int xCameraCurve;
    private int myPrivilege;
    private final int[] currentExp;
    private IndexedSprite redStone1_3;
    private IndexedSprite redStone2_3;
    private IndexedSprite redStone3_2;
    private IndexedSprite redStone1_4;
    private IndexedSprite redStone2_4;
    private Sprite mapFlag;
    private Sprite mapMarker;
    private boolean aBoolean872;
    private final int[] anIntArray873;
    private int anInt874;
    private final boolean[] aBooleanArray876;
    private int weight;
    private MouseRecorder mouseRecorder;
    private volatile boolean drawFlames;
    private String reportAbuseInput;
    private int unknownInt10;
    private boolean menuOpen;
    private int anInt886;
    private String inputString;
    private final int maxPlayers;
    private final int myPlayerIndex;
    private Player[] localPlayers;
    private int localPlayerCount;
    private int[] playerIndices;
    private int entityUpdateCount;
    private int[] playerUpdateIndices;
    private Stream[] playerAppearanceBuffers;
    private int anInt896;
    private int anInt897;
    private int friendsCount;
    private int anInt900;
    private int[][] anIntArrayArray901;
    private final int anInt902;
    private GraphicsBuffer backLeftIP1;
    private GraphicsBuffer backLeftIP2;
    private GraphicsBuffer backRightIP1;
    private GraphicsBuffer backRightIP2;
    private GraphicsBuffer backTopIP1;
    private GraphicsBuffer backVmidIP1;
    private GraphicsBuffer backVmidIP2;
    private GraphicsBuffer backVmidIP3;
    private GraphicsBuffer backVmidIP2_2;
    private byte[] aByteArray912;
    private int anInt913;
    private int crossX;
    private int crossY;
    private int crossIndex;
    private int crossState;
    private int plane;
    private final int[] currentStats;
    private static int anInt924;
    private final long[] ignoreListAsLongs;
    private boolean loadingError;
    private final int anInt927;
    private final int[] anIntArray928;
    private int[][] anIntArrayArray929;
    private Sprite aClass30_Sub2_Sub1_Sub1_931;
    private Sprite aClass30_Sub2_Sub1_Sub1_932;
    private int anInt933;
    private int anInt934;
    private int anInt935;
    private int anInt936;
    private int anInt937;
    private int anInt938;
    private static int anInt940;
    private final int[] chatTypes;
    private final String[] chatNames;
    private final String[] chatMessages;
    private int anInt945;
    private SceneGraph sceneGraph;
    private IndexedSprite[] sideIcons;
    private int menuScreenArea;
    private int menuOffsetX;
    private int menuOffsetY;
    private int menuWidth;
    private int anInt952;
    private long fromPlayer;
    private boolean aBoolean954;
    private long[] friendsNamesAsLongs;
    private int currentSong;
    private static int nodeID = 10;
    static int portOff;
    private static boolean isMembers = true;
    private static boolean lowMem;
    private volatile boolean drawingFlames;
    private int spriteDrawX;
    private int spriteDrawY;
    private final int[] anIntArray965 = {0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff};
    private IndexedSprite titleBox;
    private IndexedSprite titleButton;
    private final int[] anIntArray968;
    private final int[] anIntArray969;
    final CacheFile[] cacheIndices;
    public int configStates[];
    private boolean aBoolean972;
    private final int anInt975;
    private final int[] anIntArray976;
    private final int[] anIntArray977;
    private final int[] anIntArray978;
    private final int[] anIntArray979;
    private final int[] anIntArray980;
    private final int[] anIntArray981;
    private final int[] anIntArray982;
    private final String[] aStringArray983;
    private int anInt984;
    private int anInt985;
    private static int anInt986;
    private Sprite[] hitMarks;
    private int anInt988;
    private int anInt989;
    private final int[] anIntArray990;
    private static boolean aBoolean993;
    private final boolean aBoolean994;
    private int anInt995;
    private int anInt996;
    private int anInt997;
    private int anInt998;
    private int anInt999;
    private IsaacCipher encryption;
    private Sprite mapEdge;
    private final int anInt1002;
    static final int[][] anIntArrayArray1003 = {{6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193}, {8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239}, {25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003}, {4626, 11146, 6439, 12, 4758, 10270}, {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}};
    private String amountOrNameInput;
    private static int anInt1005;
    private int daysSinceLastLogin;
    private int packetSize;
    private int packetOpcode;
    private int anInt1009;
    private int anInt1010;
    private int anInt1011;
    private Deque projectileList;
    private int anInt1014;
    private int anInt1015;
    private int anInt1016;
    private boolean aBoolean1017;
    private int anInt1018;
    private static final int[] anIntArray1019;
    private int anInt1021;
    private int anInt1022;
    private int loadingStage;
    private IndexedSprite scrollBar1;
    private IndexedSprite scrollBar2;
    private int anInt1026;
    private IndexedSprite backBase1;
    private IndexedSprite backBase2;
    private IndexedSprite backHmid1;
    private final int[] anIntArray1030;
    private boolean aBoolean1031;
    private Sprite[] mapFunctions;
    private int baseX;
    private int baseY;
    private int anInt1036;
    private int anInt1037;
    private int loginFailures;
    private int anInt1039;
    private int anInt1040;
    private int anInt1041;
    private int dialogID;
    private final int[] maxStats;
    private final int[] anIntArray1045;
    private int anInt1046;
    private boolean aBoolean1047;
    private int anInt1048;
    private String aString1049;
    private static int anInt1051;
    private final int[] anIntArray1052;
    private Archive titleArchive;
    private int anInt1054;
    private int anInt1055;
    private Deque stillGraphicList;
    private final int[] anIntArray1057;
    private final Interface anInterface_1059;
    private IndexedSprite[] mapScenes;
    private static int drawCycle;
    private int anInt1062;
    private final int anInt1063;
    private int friendsListAction;
    private final int[] anIntArray1065;
    private int mouseInvInterfaceIndex;
    private int lastActiveInvInterface;
    private OnDemandFetcher onDemandFetcher;
    private int anInt1069;
    private int anInt1070;
    private int anInt1071;
    private int[] anIntArray1072;
    private int[] anIntArray1073;
    private Sprite mapDotItem;
    private Sprite mapDotNPC;
    private Sprite mapDotPlayer;
    private Sprite mapDotFriend;
    private Sprite mapDotTeam;
    private int anInt1079;
    private boolean aBoolean1080;
    private String[] friendsNames;
    private Stream inputStream;
    private int anInt1084;
    private int anInt1085;
    private int activeInterfaceType;
    private int anInt1087;
    private int anInt1088;
    private int chatboxScrollerPos;
    private final int[] expectedCRCs;
    private int[] menuActionCmd2;
    private int[] menuActionCmd3;
    private int[] menuActionOpcode;
    private int[] menuActionCmd1;
    private Sprite[] hintIcon;
    private Sprite[] prayerIcon;
    private Sprite[] skullIcon;
    private static int anInt1097;
    private int anInt1098;
    private int anInt1099;
    private int anInt1100;
    private int anInt1101;
    private int anInt1102;
    private boolean tabAreaAltered;
    private int systemUpdateTime;
    private GraphicsBuffer topLeftCanvas;
    private GraphicsBuffer bottomLeftCanvas;
    private GraphicsBuffer leftCanvas;
    private GraphicsBuffer leftFlameCanvas;
    private GraphicsBuffer rightFlameCanvas;
    private GraphicsBuffer titleBoxLeftCanvas;
    private GraphicsBuffer bottomRightCanvas;
    private GraphicsBuffer smallLeftCanvas;
    private GraphicsBuffer smallRightCanvas;
    private static int anInt1117;
    private int membersInt;
    private String aString1121;
    private Sprite compass;
    private GraphicsBuffer aGraphicsBuffer_1123;
    private GraphicsBuffer aGraphicsBuffer_1124;
    private GraphicsBuffer aGraphicsBuffer_1125;
    public static Player myPlayer;
    private final String[] atPlayerActions;
    private final boolean[] atPlayerArray;
    private final int[][][] anIntArrayArrayArray1129;
    private final int[] tabInterfaceIDs = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    private int anInt1131;
    private int anInt1132;
    private int menuActionCount;
    private static int anInt1134;
    private int spellSelected;
    private int anInt1137;
    private int spellUsableOn;
    private String spellTooltip;
    private Sprite[] aClass30_Sub2_Sub1_Sub1Array1140;
    private boolean aBoolean1141;
    private static int anInt1142;
    private IndexedSprite redStone1;
    private IndexedSprite redStone2;
    private IndexedSprite redStone3;
    private IndexedSprite redStone1_2;
    private IndexedSprite redStone2_2;
    private int energy;
    private boolean aBoolean1149;
    private Sprite[] crosses;
    private boolean musicEnabled;
    private IndexedSprite[] runes;
    private boolean needDrawTabArea;
    private int unreadMessages;
    private static int anInt1155;
    private static boolean fpsIsOn;
    public boolean loggedIn;
    private boolean canMute;
    private boolean aBoolean1159;
    private boolean aBoolean1160;
    static int loopCycle;
    private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    private GraphicsBuffer aGraphicsBuffer_1163;
    private GraphicsBuffer minimapCanvas;
    private GraphicsBuffer gameScreenCanvas;
    private GraphicsBuffer aGraphicsBuffer_1166;
    private int daysSinceRecovChange;
    private Socket socketStream;
    private int anInt1169;
    private int minimapInt3;
    private int anInt1171;
    private long aLong1172;
    private String myUsername;
    private String myPassword;
    private static int anInt1175;
    private boolean genericLoadingError;
    private final int[] anIntArray1177 = {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
    private int reportAbuseInterfaceID;
    private Deque aClass19_1179;
    private int[] anIntArray1180;
    private int[] anIntArray1181;
    private int[] anIntArray1182;
    private byte[][] aByteArrayArray1183;
    private int cameraY;
    private int cameraX;
    private int anInt1186;
    private int anInt1187;
    private static int anInt1188;
    private int invOverlayInterfaceID;
    private int[] anIntArray1190;
    private int[] anIntArray1191;
    private Stream outputStream;
    private int anInt1193;
    private int splitPrivateChat;
    private IndexedSprite invBack;
    private IndexedSprite mapBack;
    private IndexedSprite chatBack;
    private String[] menuActionName;
    private Sprite aClass30_Sub2_Sub1_Sub1_1201;
    private Sprite aClass30_Sub2_Sub1_Sub1_1202;
    private final int[] anIntArray1203;
    static final int[] anIntArray1204 = {9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486};
    private static boolean flagged;
    private final int[] anIntArray1207;
    private int flameCycle;
    private int minimapInt2;
    private int anInt1210;
    private int anInt1211;
    private String promptInput;
    private int anInt1213;
    private int[][][] intGroundArray;
    private long aLong1215;
    private int loginScreenCursorPos;
    private final IndexedSprite[] modIcons;
    private long aLong1220;
    private int tabID;
    private int anInt1222;
    private boolean inputTaken;
    private int inputDialogState;
    private static int anInt1226;
    private int nextSong;
    private boolean songChanging;
    private final int[] anIntArray1229;
    private CollisionMap[] collisionMaps;
    public static int anIntArray1232[];
    private boolean aBoolean1233;
    private int[] anIntArray1234;
    private int[] anIntArray1235;
    private int[] anIntArray1236;
    private int anInt1237;
    private int anInt1238;
    public final int anInt1239 = 100;
    private final int[] anIntArray1240;
    private final int[] anIntArray1241;
    private boolean aBoolean1242;
    private int atInventoryLoopCycle;
    private int atInventoryInterface;
    private int atInventoryIndex;
    private int atInventoryInterfaceType;
    private byte[][] aByteArrayArray1247;
    private int tradeMode;
    private int anInt1249;
    private final int[] anIntArray1250;
    private int anInt1251;
    private final boolean alreadyLoaded;
    private int anInt1253;
    private int anInt1254;
    private boolean welcomeScreenRaised;
    private boolean messagePromptRaised;
    private int anInt1257;
    private byte[][][] byteGroundArray;
    private int prevSong;
    private int destX;
    private int destY;
    private Sprite minimap;
    private int anInt1264;
    private int flashEffectCycle;
    private String loginMessage1;
    private String loginMessage2;
    private int localX;
    private int localY;
    private Font smallFont;
    private Font aFont_1271;
    private Font boldFont;
    private int anInt1275;
    private int backDialogID;
    private int anInt1278;
    private int anInt1279;
    private int[] bigX;
    private int[] bigY;
    private int itemSelected;
    private int anInt1283;
    private int anInt1284;
    private int anInt1285;
    private String selectedItemName;
    private int publicChatMode;
    private static int anInt1288;
    private int anInt1289;
    public static int anInt1290;

    static {
        anIntArray1019 = new int[150];
        int i = 0;
        for (int j = 0; j < 150; j++) {
            int l = j + 1;
            int i1 = (int) ((double) l + 300D * Math.pow(2D, (double) l / 7D));
            i += i1;
            Client.anIntArray1019[j] = i / 4;
        }
        Client.anIntArray1232 = new int[32];
        i = 2;
        for (int k = 0; k < 32; k++) {
            Client.anIntArray1232[k] = i - 1;
            i += i;
        }
    }
}
