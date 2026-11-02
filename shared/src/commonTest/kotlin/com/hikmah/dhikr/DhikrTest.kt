package com.hikmah.dhikr

import com.hikmah.dhikr.content.DhikrCatalog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DhikrTest {
    @Test
    fun catalog_hasSubhanallah() {
        val d = assertNotNull(DhikrCatalog.byId("subhanallah"))
        assertEquals("سُبْحَانَ اللَّهِ", d.arabic)
        assertEquals("Glory be to Allah.", d.translation)
        assertEquals(DhikrKind.REPEATED, d.kind)
    }

    @Test
    fun greeting_isNonEmpty() {
        assertTrue(greeting().isNotBlank())
    }
}
