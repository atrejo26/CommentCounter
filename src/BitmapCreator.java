import java.util.*;
import java.io.*;

/**
 * This class is used to programmatically build a bitmap image that can
 * be viewed by any compliant viewer. One creates an image and then sets
 * the pixels that make up that image. In the constructor one sets the
 * height and width of the image and then uses addPixel() to add a pixel
 * to an image.
 * See http://www.fortunecity.com/skyscraper/windows/364/bmpffrmt.html 
 * for the specification of what a bitmap is
 * 
 * @author Mark Mahoney mmahoney@carthage.edu
 *
 */
public class BitmapCreator 
{
    //info for the file header
    private List < String > bitMapFileHeader = new ArrayList< String >();
    //info for the info header
    private List < String > bitMapInfoHeader = new ArrayList< String >();
    //info for the pixel info
    private List < String > pixels = new ArrayList < String >();
    
    //height, width, and bits per color in the bitmap
    private int numCols;
    private int numRows;
    private int extraBytesInRow;
    
    //constants that are used in the headers
    public final static int BITMAPFILEHEADER_SIZE = 14;
    public final static int BITMAPINFOHEADER_SIZE = 40;
    public final static int BITS_PER_COLOR = 24;
    public final static int LETTER_B = 66;
    public final static int LETTER_M = 77;
    
    /**
     * Create a bitmap with a height and width in pixels and 24 bits per color
     */
    public BitmapCreator(int nRows, int nCols)
    {
        //set the height, width, and bits per color
        setNumCols(nCols);
        setNumRows(nRows);
        
        //this is the number of extra bytes needed in a row to make it divisible by 4
        //this is a rule of the bitmap file format 
        extraBytesInRow = ((getNumCols() * 3) % 4 == 0) ? (0) : (4 - ((getNumCols() * 3) % 4));
        
        //add all white pixels to the pixel array
        addPixels();
        
        //set up the file header and info header
        setUpFileHeader();
        setUpInfoHeader();
    }
    
    /**
     * Set the width of the image in pixels
     * @param w Width in pixels
     */
    public void setNumCols(int w)
    {
        //if there is a positive width
        if(w > 0)
        {
            //store passed in value
            numCols = w;
        }
        else //can't have non-positive width, default to 100 pixels
        {
            numCols = 100;
        }
    }
    
    /**
     * Gets the width of the image in pixels
     * @return Width of bitmap in pixels
     */
    public int getNumCols()
    {
        return numCols;
    }
    
    /**
     * Set the height of the image in pixels
     * @param w Height in pixels
     */
    public void setNumRows(int h)
    {
        //if there is a positive height
        if(h > 0)
        {
            //store passed in value
            numRows = h;
        }
        else //can't have non-positive height, default to 100 pixels
        {
            numRows = 100;
        }
    }
    
    /**
     * Get the height in pixels
     * @return Height of bitmap in pixels
     */
    public int getNumRows()
    {
        return numRows;
    }
    
    /**
     * Gets the size of the file in bytes
     * @return Size of file in bytes
     */
    public int getFileSize()
    {
        //get the size of the image from the pixels
        int sum = getImageSize();
        
        //add the size of the headers
        sum += BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE;
        
        return sum;
    }
    
    /**
     * Get the size of the image only
     * @return The size of the image in bytes
     */
    public int getImageSize()
    {
        int sum = 0;
        
        //go through all the pixels
        for(int i = 0;i < pixels.size();i++)
        {
            //add the sum of each of the pixels
            sum += pixels.get(i).length();
        }
        
        //divide by eight to get it into bytes
        return sum / 8;
    }
    
    /**
     * Create a bitmap info header. 
     */
    public void setUpInfoHeader() 
    {
        //for bi size 4 byte int
        bitMapInfoHeader.add(swapBytes32Bit(convertIntToBinaryString(BitmapCreator.BITMAPINFOHEADER_SIZE, 32)));
        
        //for bi width 4 byte int
        bitMapInfoHeader.add(swapBytes32Bit(convertIntToBinaryString(getNumCols(), 32)));
        
        //for bi height 4 byte int
        bitMapInfoHeader.add(swapBytes32Bit(convertIntToBinaryString(getNumRows(), 32)));
        
        //for bi planes 2 byte int
        bitMapInfoHeader.add(convertIntToBinaryString(1, 8));
        bitMapInfoHeader.add(convertIntToBinaryString(0, 8));
        
        //for bi bit count 2 byte int
        bitMapInfoHeader.add(convertIntToBinaryString(BitmapCreator.BITS_PER_COLOR, 8));
        bitMapInfoHeader.add(convertIntToBinaryString(0, 8));
        
        //for bi compression 4 byte int
        bitMapInfoHeader.add(convertIntToBinaryString(0, 32));
        
        //for bi size image 4 byte int
        bitMapInfoHeader.add(swapBytes32Bit(convertIntToBinaryString(getImageSize(), 32)));
        
        //for bi x pixels per meter 4 byte int
        bitMapInfoHeader.add(convertIntToBinaryString(0, 32));
        
        //for bi y pixels per meter 4 byte int
        bitMapInfoHeader.add(convertIntToBinaryString(0, 32));
        
        //for bi clr used 4 byte int
        bitMapInfoHeader.add(convertIntToBinaryString(0, 32));
        
        //for bi clr important 4 byte int
        bitMapInfoHeader.add(convertIntToBinaryString(0, 32));
    }
    
    /**
     * Create a bitmap file header. 
     */
    public void setUpFileHeader() 
    {
        //for bfType- should be BM for bitmap- two byte equivalent of ASCII 'B''M'
        bitMapFileHeader.add(convertIntToBinaryString(BitmapCreator.LETTER_B, 8)); //ASCII 'B'
        bitMapFileHeader.add(convertIntToBinaryString(BitmapCreator.LETTER_M, 8)); //ASCII 'M' 
        
        //for bfSize- size of file in bytes 4 byte int
        bitMapFileHeader.add(swapBytes32Bit(convertIntToBinaryString(getFileSize(), 32)));
        
        //for bfReserved 1 and 2- unused should be 0 2 byte int
        bitMapFileHeader.add(convertIntToBinaryString(0, 16));
        
        bitMapFileHeader.add(convertIntToBinaryString(0, 16));
        
        //for bfOffBits- offset into the real data 4 byte int
        bitMapFileHeader.add(swapBytes32Bit(convertIntToBinaryString(BitmapCreator.BITMAPFILEHEADER_SIZE + BitmapCreator.BITMAPINFOHEADER_SIZE, 32)));
    }
    
    /**
     * Adds a pixel to the image
     * @param row row number to add the pixel
     * @param col column number to add the pixel
     * @param r red value (0 - 255) 
     * @param g green value (0 - 255)
     * @param b blue value (0 - 255)
     */
    public void addPixel(int row, int col, int r, int g, int b)
    {
        //check the row and column values first
        if((row >= 0 && row < getNumRows()) && (col >= 0 && col < getNumCols()) )
        {
            //find the number of bytes in a row
            int numBytesInRow = (getNumCols() * 3) + extraBytesInRow;
            
            //find the position if a given pixel
            int posOfPixel = (((getNumRows() - 1) - row) * numBytesInRow) + (col * 3);
            
            //add the color to a row and col
            pixels.set(posOfPixel, convertIntToBinaryString(b, 8));
            pixels.set(posOfPixel + 1, convertIntToBinaryString(g, 8));
            pixels.set(posOfPixel + 2, convertIntToBinaryString(r, 8));
        }
    }
    /**
     * Add some all white pixels to the image when the image is created
     */
    public void addPixels()
    {
        //for every row
        for(int i = 0;i < getNumRows();i++)
        {
            //for every column
            for(int j = 0;j < getNumCols();j++)
            {
                //add white pixels
                pixels.add(convertIntToBinaryString(255, 8));
                pixels.add(convertIntToBinaryString(255, 8));
                pixels.add(convertIntToBinaryString(255, 8));
            }
            
            //now add the extra bytes to make the number of bytes 
            //in a row divisible by four
            for(int k = 0;k < extraBytesInRow;k++)
            {
                //add dummy 0's
                pixels.add(convertIntToBinaryString(0, 8));
            }
        }
    }
    
    /**
     * Write the image to a file
     * @param filename Filename of the bitmap image
     */
    public void writeToFile(String filename)
    {
        try 
        {
            //create a File object representing the file
            File outputFile = new File(filename);
            FileOutputStream out = new FileOutputStream(outputFile);
            
            //holds the bits from the image
            StringBuffer bits = new StringBuffer();       
        
            System.out.print("File Header: ");
            
            //add the bit map header file bits
            for(int i = 0;i < bitMapFileHeader.size();i++)
            {
                System.out.print( Long.toHexString(Long.parseLong(bitMapFileHeader.get(i), 2)) + " " );
                bits.append(bitMapFileHeader.get(i));
            }
            System.out.println();
            
            System.out.print("Info Header: ");
            //add the bit map info header bits
            for(int i = 0;i < bitMapInfoHeader.size();i++)
            {
                bits.append(bitMapInfoHeader.get(i));
                System.out.print( Long.toHexString(Long.parseLong(bitMapInfoHeader.get(i), 2)) + " ");
            }
            System.out.println();
            
            System.out.print("Pixels: ");
            //add the bit map info header bits
            for(int i = 0;i < pixels.size(); i++)
            {
                System.out.print( Long.toHexString(Long.parseLong(pixels.get(i), 2)) + " ");
                bits.append(pixels.get(i));
            }
            System.out.println();
            
            //convert the bits to one big string
            //String bits2 = bits.toString();
            
            //for every bit in the string buffer (add 8 each iteration)
            for(int i = 0;i < bits.length();i = i + 8)
            {
                StringBuffer b = new StringBuffer();
                
                //grab eight bits
                for(int j = 0;j < 8;j++)
                {
                    b.append(bits.charAt(i + j));
                }
                
                System.out.println(Long.toHexString(Long.parseLong(b.toString(), 2)));
                //write 8 bits to the file
                out.write(Integer.parseInt(b.toString(), 2));
            }
            
            //close the file
            out.close();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }
    /**
     * This method swaps two groups of 8 bits for storing 
     * @param bits A string with the bits to swap
     * @return A string with the swapped bits
     */
    public String swapBytes16Bit(String bits)
    {
        //grab the first 8 and the next 8
        String temp1 = bits.substring(0, 8);
        String temp2 = bits.substring(8, 16);
        
        //swap them
        return temp2.concat(temp1);
    }
    
    /**
     * This method swaps four groups of 8 bits for storing 
     * @param bits A string with the bits to swap
     * @return A string with the swapped bits
     */    
    public String swapBytes32Bit(String bits)
    {
        //grab four groups of bits
        String temp1 = bits.substring(0, 8);
        String temp2 = bits.substring(8, 16);
        String temp3 = bits.substring(16, 24);
        String temp4 = bits.substring(24, 32);
        
        //swap them
        temp2 = temp2.concat(temp1);
        temp4 = temp4.concat(temp3);
        
        //swap them one more time
        return temp4.concat(temp2);
    }
    
    /**
     * This method takes in a number and converts it to a series of bits
     * stored in a string. The method will pad the string with 0's to fill
     * the exact number of bits
     * @param number Integer number to convert to bits
     * @param numBits Number of bits in the string.
     * @return A string with the binary number
     */
    public String convertIntToBinaryString(int number, int numBits)
    {
        //list of bytes for the given int
        String retVal = null;
        
        //add 31 0's and the binary string of the number passed in (the number passed in
        //will produce at least one additional bit to make 32 total bits)
        StringBuffer numberBuffer = new StringBuffer("0000000000000000000000000000000" + Integer.toBinaryString(number));
        
        //reverse the buffer, convert to a string, and pull out the first numBits bits
        String revBits = numberBuffer.reverse().toString().substring(0, numBits);
        
        //create a new buffer with the reverse bits
        numberBuffer = new StringBuffer(revBits);
        
        //reverse them back
        numberBuffer.reverse();
    
        //create a string from them
        retVal = numberBuffer.toString();
        
        return retVal;
    }
    public static void main(String[] args) 
    {
        BitmapCreator bm = new BitmapCreator(13, 17);
        
        //bm.addPixel(0, 0, 0, 0, 0);
        //bm.addPixel(0, 16, 0, 0, 0);
        //bm.addPixel(12, 0, 0, 0, 0);
        //bm.addPixel(12, 16, 0, 0, 0);
        
        
        /*
        for(int i = 0;i < 10;i++)
        {
            bm.addPixel(i, i, 0, 0, 0);
        }
        for(int i = 0;i < 10;i++)
        {
            bm.addPixel(9 - i, i, 0, 100, 0);
        }
        */
        
        for(int i = 0;i < 13;i++)
        {
            for(int j = 0;j < 17;j++)
            {
                if(i == 0 || i == 12 || j == 0 || j == 16)
                {
                    bm.addPixel(i, j, 0, 0, 0);
                }
            }
        }
        
        
        
        bm.writeToFile("markfile.bmp");
        
    }
}
