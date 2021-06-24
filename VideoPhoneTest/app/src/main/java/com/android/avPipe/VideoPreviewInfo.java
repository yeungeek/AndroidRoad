/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        VideoPreviewInfo.java
**
**  PURPOSE     Hold video preview window coordinates
**
**==========================================================================*/

package com.android.avPipe;

/**
 * Discription Here
 * <p>
 */
public class VideoPreviewInfo
{

    /**
     * video preview view position start x
     */
    private int posX;

    /**
     * video preview view position start y
     */
    private int posY;

    /**
     * video preview width
     */
    private int width;

    /**
     * video preview height
     */
    private int height;

    public int getPosX()
    {
        return posX;
    }

    public void setPosX(int posX)
    {
        this.posX = posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public void setPosY(int posY)
    {
        this.posY = posY;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }
}
