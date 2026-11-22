package com.example.yaza_wanga

import com.example.yaza_wanga.util.DriveLinks
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DriveLinksTest {

    @Test
    fun testIsDriveLink() {
        assertTrue(DriveLinks.isDriveLink("https://drive.google.com/file/d/12345/view?usp=sharing"))
        assertTrue(DriveLinks.isDriveLink("https://docs.google.com/document/d/abcde/edit"))
        assertFalse(DriveLinks.isDriveLink("https://youtube.com/watch?v=12345"))
    }

    @Test
    fun testExtractDriveFileId_threeShapes() {
        // Shape 1: file/d/<ID>/view
        val url1 = "https://drive.google.com/file/d/1AbC-De_F99/view?usp=sharing"
        assertEquals("1AbC-De_F99", DriveLinks.extractDriveFileId(url1))

        // Shape 2: open?id=<ID>
        val url2 = "https://drive.google.com/open?id=2Xyz_77ABC"
        assertEquals("2Xyz_77ABC", DriveLinks.extractDriveFileId(url2))

        // Shape 3: uc?id=<ID>
        val url3 = "https://drive.google.com/uc?id=3PqrS_99XYZ&export=download"
        assertEquals("3PqrS_99XYZ", DriveLinks.extractDriveFileId(url3))
    }

    @Test
    fun testNormalizeDriveLink() {
        val driveUrl = "https://drive.google.com/file/d/1AbC-De_F99/view?usp=sharing"
        val normalized = DriveLinks.normalizeDriveLink(driveUrl)
        assertEquals("https://drive.google.com/uc?export=download&id=1AbC-De_F99", normalized)

        val normalUrl = "https://example.com/notes.pdf"
        assertEquals(normalUrl, DriveLinks.normalizeDriveLink(normalUrl))
    }

    @Test
    fun testEmptyInput() {
        assertFalse(DriveLinks.isDriveLink(""))
        assertNull(DriveLinks.extractDriveFileId(""))
        assertEquals("", DriveLinks.normalizeDriveLink(""))
        assertFalse(DriveLinks.isValidMaterialLink(""))
    }

    @Test
    fun testIsValidMaterialLink() {
        assertTrue(DriveLinks.isValidMaterialLink("https://drive.google.com/file/d/123"))
        assertTrue(DriveLinks.isValidMaterialLink("http://example.com"))
        assertFalse(DriveLinks.isValidMaterialLink("ftp://example.com"))
        assertFalse(DriveLinks.isValidMaterialLink("not-a-url"))
    }
}
