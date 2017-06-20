package edu.barsu.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MacroHash {
    public static void main(String[] args) {}

    private static final String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();

        for (char tempChar: chars)
            hex.append(Integer.toHexString((int) tempChar));

        return hex.toString();
    }

    private static final ArrayList<String> hexToAscii(String hexValue) {
        ArrayList<String> hexList = new ArrayList<String>();
        boolean isFirstPart = true;
        String tempHex = "";

        for (char tempChar: hexValue.toCharArray()) {
            if (isFirstPart)
                tempHex = String.valueOf(tempChar);
            else
                hexList.add(tempHex + String.valueOf(tempChar));

            isFirstPart = !isFirstPart;
        }

        return  hexList;
    }

    private static final int getHashSumm(String data) {
        int summ = 0;

        for (String hexLine: hexToAscii(data))
            summ += Integer.parseInt(hexLine, 16);

        return Double.valueOf(summ / 3.1415927).intValue();
    }

    private static final String getMacroSumm(String source) {
        String hexSource = asciiToHex(source);

        ArrayList<String> hexList = hexToAscii(hexSource);
        //
        Double c = Double.valueOf(0);

        for (String tempHex: hexList) {
            Double x = Double.valueOf(Integer.parseInt(tempHex, 16));

            c += (x + source.length() * x + getHashSumm(hexSource) / x);
        }
        //
        String completeSumm = "";

        if (String.valueOf(c).length() <= 32) {
            for (String tempHex: hexList) {
                Double x = Double.valueOf(Integer.parseInt(tempHex, 16));
                Double sx = Double.valueOf(completeSumm.length());

                completeSumm += (sx.intValue() * getHashSumm(hexSource) * x);
            }

        } else {
            for (String tempHex: hexList) {
                Double x = Double.valueOf(Integer.parseInt(tempHex, 16));
                Double sx = Double.valueOf(completeSumm.length());

                completeSumm += (sx.intValue() * getHashSumm(hexSource) * x);
            }
        }

        return completeSumm;
    }

    public static final String getHash(String source, String ... algorithmName)
            throws NumberFormatException, NoSuchAlgorithmException {
        String summ = getMacroSumm(source);

        MessageDigest md = MessageDigest.getInstance((algorithmName.length == 0)?"SHA-256":algorithmName[0]);

        md.update(asciiToHex(asciiToHex(summ)).getBytes());
        byte[] digest = md.digest();

        return String.format("%064x", new java.math.BigInteger(1, digest));
    }
}
