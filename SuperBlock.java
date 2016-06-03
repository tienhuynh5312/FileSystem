class SuperBlock
{
   public int totalBlocks; // the number of disk blocks
   public int totalInodes; // the number of inodes
   public int freeList;    // the block number of the free list's head

    // Constructor
   public SuperBlock(int diskSize)
   {
       totalBlocks = diskSize / Disk.blockSize;
       totalInodes = 0;
       freeList = 1;
   }

   // Constructor: grab

    // Formats the disk
    // fileCount: number of inode blocks that the disk needs to free up
   void format (int fileCount)
   {
       totalInodes = fileCount;
       freeList = (fileCount / 16) + 1;

       // create the freeList linked list
       byte[] block = new byte[Disk.blockSize];
       for (int i = freeList; i < totalBlocks - 1; i++)
       {
           // get this block from disk
           SysLib.rawread(i, block);
           // set its first four bytes to point to the next free block
           SysLib.int2bytes(i + 1, block, 0);
           // write block back to disk
           SysLib.rawwrite(i, block);
       }
       // set the last block's pointer to -1
       setLastBlock(block);
   }

    // Writes totalBlocks, totalInodes, and the freeList back to the Disk
    void sync ()
    {
        // create a new byte array to be copied to disk
        byte[] block = new byte[Disk.blockSize];

        // write totalBlocks to the block
        SysLib.int2bytes(totalBlocks, block, 0);
        // write totalInodes to the block
        SysLib.int2bytes(totalInodes, block, 4);
        // write the freeList to the block
        SysLib.int2bytes(freeList, block, 8);
    }

    // Finds and returns the blockID of the next free block from the freeList
    int getBlock()
    {
        return freeList;
    }

    // Add the blockID to the end of the freeList
    boolean returnBlock (int blockID)
    {
        if (blockID < totalBlocks)
        {
            // do something
            return true;
        }
        return false;
    }

    // Sets the last block's free pointer during formatting
    void setLastBlock(byte[] block)
    {
        // get the last disk block
        SysLib.rawread(999, block);
        // make it point to a non existant disk block
        SysLib.int2bytes(-1, block, 0);
        // write last disk block back to disk
        SysLib.rawwrite(999, block);
    }

    // Sets the freeList to the passed in int
    boolean setFreeList(int index)
    {
        if (index > 0 && index < 1000)
        {
            freeList = index;
            return true;
        }
        else
        {
            return false;
        }
    }
}

/*
* Questions:
*
*
*/
