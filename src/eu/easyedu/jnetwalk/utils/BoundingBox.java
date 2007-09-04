/*
 * BoundingBox.java
 * 
 * Created on 2.9.2007, 19:21:28
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.easyedu.jnetwalk.utils;

/**
 *
 * @author hlavki
 */
public enum BoundingBox {
    /**
     * Constant to specify that the bounding box should be not be changed
     * for any angle of rotation when creating the rotated image.  This
     * means the returned image will be the same size as the given image.
     * Of course, that also means the corners of the image may be cut off.
     */
    NO_BOX,
    
    /**
     * Constant to specify that the exact bounding box should be used for
     * the specified angle of rotation when creating the rotated image.
     * This is the default option.  When used, the rotated image may be
     * larger then the source image, but no larger then needed to fit the
     * rotated image exactly.  Therefore, rotating the same image to various
     * angles may result in varying image sizes.
     */
    EXACT,
    
    /**
     * Constant to specify that the largest bounding box should be used when
     * creating the rotated image.  When used, the rotated image will be
     * larger then the source image, but all rotated images of that same
     * source image will be the same size, regardless of the angle of
     * rotation.  This may result in significant "empty space" between the
     * edge of the returned image and the actual drawn pixel areas.
     */
    LARGEST
}
