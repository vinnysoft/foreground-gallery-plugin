/*
	    Copyright 2012 Bruno Carreira - Lucas Farias - Rafael Luna - Vin�cius Fonseca. 

		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

		Unless required by applicable law or agreed to in writing, software
		distributed under the License is distributed on an "AS IS" BASIS,
		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		See the License for the specific language governing permissions and
   		limitations under the License.   			
*/

package com.foregroundgalleryplugin;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import org.apache.commons.codec.binary.Base64;
import org.apache.cordova.CameraLauncher;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;

public class ForegroundGalleryLauncher extends CameraLauncher
{

	private int mQuality; // Compression quality hint (0-100: 0=low quality &
    // high compression, 100=compress of max quality)
    private int targetWidth; // desired width of the image
    private int targetHeight; // desired height of the image	
    public String callbackId;
       
    /**
     * Executes the request and returns PluginResult.
     * 
     * @param action
     *            The action to execute.
     * @param args
     *            JSONArry of arguments for the plugin.
     * @param callbackId
     *            The callback id used when calling back into JavaScript.
     * @return A PluginResult object with a status and message.
     */
    public PluginResult execute(String action, JSONArray args, String callbackId)
    {
        this.callbackId = callbackId;

        try
        {
            this.targetHeight = 0;
            this.targetWidth = 0;
            JSONObject options = args.optJSONObject(0);
            if (options != null)
            {
                this.targetHeight = options.getInt("targetHeight");
                this.targetWidth = options.getInt("targetWidth");
                options.getInt("quality");
            }

            this.getImage();

            PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
            r.setKeepCallback(true);
            return r;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
        }
    }

    /**
     * Get image from photo library.
     */
    public void getImage()
    {
        Intent intent = new Intent(this.ctx.getContext() , GalleryActivity.class);
        this.ctx.startActivityForResult((Plugin) this, intent, 11);
    }

    /**
     * Called when the camera view exits.
     * 
     * @param requestCode
     *            The request code originally supplied to
     *            startActivityForResult(), allowing you to identify who this
     *            result came from.
     * @param resultCode
     *            The integer result code returned by the child activity through
     *            its setResult().
     * @param intent
     *            An Intent, which can return result data to the caller (various
     *            data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {

        if (resultCode == Activity.RESULT_OK)
        {

            Uri uri = intent.getData();
            ContentResolver resolver = this.ctx.getContentResolver();

            try
            {
                Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(resolver.openInputStream(uri));
                bitmap = scaleBitmap(bitmap);
                this.processPicture(bitmap);
                bitmap.recycle();
                bitmap = null;
                System.gc();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                this.failPicture("Error retrieving image.");
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED)
        {
            this.failPicture("Selection cancelled.");
        }
        else
        {
            this.failPicture("Selection did not complete!");
        }
    }
    
    public void processPicture(Bitmap bitmap)
    {
        ByteArrayOutputStream jpeg_data = new ByteArrayOutputStream();
        try
        {
            if (bitmap.compress(CompressFormat.JPEG, mQuality, jpeg_data))
            {
                byte[] code = jpeg_data.toByteArray();
                byte[] output = Base64.encodeBase64(code);
                String js_out = new String(output);
                this.success(new PluginResult(PluginResult.Status.OK,
                        "data:image/jpeg;base64," + js_out), this.callbackId);
                js_out = null;
                output = null;
                code = null;
            }
        }
        catch (Exception e)
        {
            this.failPicture("Error compressing image.");
        }
        jpeg_data = null;
    }

    /**
     * Scales the bitmap according to the requested size.
     * 
     * @param bitmap        The bitmap to scale.
     * @return Bitmap       A new Bitmap object of the same bitmap after scaling. 
     */
    public Bitmap scaleBitmap(Bitmap bitmap)
    {
        int newWidth = this.targetWidth;
        int newHeight = this.targetHeight;
        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();

        // If no new width or height were specified return the original bitmap
        if (newWidth <= 0 && newHeight <= 0)
        {
            return bitmap;
        }
        // Only the width was specified
        else if (newWidth > 0 && newHeight <= 0)
        {
            newHeight = (newWidth * origHeight) / origWidth;
        }
        // only the height was specified
        else if (newWidth <= 0 && newHeight > 0)
        {
            newWidth = (newHeight * origWidth) / origHeight;
        }
        // If the user specified both a positive width and height
        // (potentially different aspect ratio) then the width or height is
        // scaled so that the image fits while maintaining aspect ratio.
        // Alternatively, the specified width and height could have been
        // kept and Bitmap.SCALE_TO_FIT specified when scaling, but this
        // would result in whitespace in the new image.
        else
        {
            double newRatio = newWidth / (double) newHeight;
            double origRatio = origWidth / (double) origHeight;

            if (origRatio > newRatio)
            {
                newHeight = (newWidth * origHeight) / origWidth;
            }
            else if (origRatio < newRatio)
            {
                newWidth = (newHeight * origWidth) / origHeight;
            }
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

}
