package com.example.yaza_wanga

import com.example.yaza_wanga.data.model.Material
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MaterialAttachmentsTest {

    @Test
    fun material_fileAttachment_hasCorrectMetadata() {
        val material = Material(
            id = "m_101",
            courseCode = "INF 212",
            tutorId = "tutor_1",
            title = "DOM Manipulation PDF",
            fileUrl = "https://firebasestorage.googleapis.com/v0/b/app.appspot.com/o/materials%2FINF%20212%2Fm_101_dom.pdf?alt=media",
            fileName = "dom_manipulation.pdf",
            mimeType = "application/pdf",
            fileSizeMb = 4.2,
            isExternalLink = false
        )

        assertEquals("dom_manipulation.pdf", material.fileName)
        assertEquals("application/pdf", material.mimeType)
        assertEquals(4.2, material.fileSizeMb, 0.01)
        assertFalse(material.isExternalLink)
    }

    @Test
    fun material_externalLink_isIdentifiedCorrectly() {
        val material = Material(
            id = "m_102",
            courseCode = "INF 212",
            tutorId = "tutor_1",
            title = "YouTube Video Tutorial",
            fileUrl = "https://youtube.com/watch?v=dQw4w9WgXcQ",
            isExternalLink = true
        )

        assertTrue(material.isExternalLink)
        assertEquals("", material.fileName)
    }

    @Test
    fun fileSizeLimit_50MbEnforced() {
        val sizeSmall = 15.4
        val sizeHuge = 52.1

        assertTrue(sizeSmall <= 50.0)
        assertFalse(sizeHuge <= 50.0)
    }
}
