package classes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class BufferReader {

	//typedef unsigned char uchar;

	//typedef std::vector<uchar> ByteVector;
	// ByteVector mData;
	private byte[] mData;
	
	private int				mDataBitSize;
	private int				mReadBitPos;
	private int				mWriteBitPos;
	
	public BufferReader() {
		mDataBitSize = 0;
		mReadBitPos = 0;
		mWriteBitPos = 0;	
	}

	public void SetData(byte[] thePtr, int theCount)
	{
		//mData.clear();
		//mData.reserve(theCount);
		//mData.insert(mData.begin(), thePtr, thePtr + theCount);
		mData = null;
		mData = thePtr;
		mDataBitSize = mData.length * 8;
	}
	
	public byte[] GetDataPtr()
	{
		if (mData.length == 0)
			return null;
		return mData;
	}
	
	public int GetDataLen() {
		return (mDataBitSize + 7) / 8; // Round up
	}
	
	public void Clear()
	{
		mReadBitPos = 0;
		mWriteBitPos = 0;
		mDataBitSize = 0;
		mData = null;
	}
	
	public byte ReadByte() {
		if ((mReadBitPos + 7) / 8 >= ((int) mData.length))
		{		
			return 0; // Underflow
		}
		
		if (mReadBitPos % 8 == 0)
		{
			byte b = mData[mReadBitPos/8];
			mReadBitPos += 8;
			return b;
		}
		else
		{
			int anOfs = mReadBitPos % 8;
			
			byte b = 0;
			
			b = (byte) (mData[mReadBitPos/8] >> anOfs);
			b |= mData[(mReadBitPos/8)+1] << (8 - anOfs);
			
			mReadBitPos += 8;		
			
			return b;
		}
	}
	
	public boolean ReadBoolean() {
		return ReadByte() != 0;
	}
	
	public short ReadShort() {
//		short aShort = ReadByte();
//		aShort |= ((short) ReadByte() << 8);
//		return aShort;
		
		byte[] holder = new byte[2];
		holder[0] = ReadByte();
		holder[1] = ReadByte();
		return ByteBuffer.wrap(holder).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	public long ReadLong() {
//		long aLong = 0;
//		aLong |= ((long) ReadByte()) << 0;
//		aLong |= ((long) ReadByte()) << 8;
//		aLong |= ((long) ReadByte()) << 16;
//		aLong |= ((long) ReadByte()) << 24;
//		return aLong;
		
		return ((long) readInt());
	}
	
	public int readInt() {
		byte[] bytes = new byte[4]; 
		for (int i = 0; i < 4; i++) {
			bytes[i] = ReadByte();
		}
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	
	
	public String ReadString() {
		String aString = "";
		int aLen = ReadShort();
		
		for (int i = 0; i < aLen; i++)
			aString += (char) ReadByte();
		
		return aString;
	}
}
