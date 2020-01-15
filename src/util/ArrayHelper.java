package util;

public class ArrayHelper {
    public static byte[] slice(byte[] arr, int start, int end) {
        byte[] slice = new byte[end - start]; 
        for (int i = 0; i < slice.length; i++) { 
            slice[i] = arr[start + i]; 
        }
        return slice; 
    }
    
    public static byte[] merge(byte[] arr1, byte[] arr2) {
        byte[] arr = new byte[arr1.length + arr2.length];
        int i = 0;
        for (; i < arr1.length; i++) {
            arr[i] = arr1[i];
        }
        for (int c = 0; c < arr2.length; c++) {
            arr[i + c] = arr2[c];
        }
        return arr;
    }
}
