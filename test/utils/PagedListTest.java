package utils;

import components.PagedList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by daniel.rothig on 06/10/2016.
 *
 * Tests for PagedList
 */
public class PagedListTest {

    @Test
    public void OutOfBoundsHasCappedBoundsAndPages() throws Exception {
        PagedList<Integer> list = new PagedList<>(new ArrayList<>(), 50, 10, 10);


        assertEquals(false, list.canPage());
        assertEquals(10, list.pageNumber());
        assertEquals(50, list.totalSize());

        assertFalse(list.canGoBack());
        assertFalse(list.canGoNext());
    }

    @Test
    public void FullProvidesCorrectMetadata() throws Exception {
        PagedList<Integer> list = new PagedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 500, 10, 10);

        assertEquals(true, list.canPage());
        assertEquals(true, list.canGoBack());
        assertEquals(true, list.canGoNext());

        assertEquals(10, list.pageNumber());
        assertEquals(500, list.totalSize());
        assertEquals(10, list.size());

        assertTrue(list.canGoBack());
        assertTrue(list.canGoNext());
    }

    @Test
    public void CantGoBackAtStart() throws Exception {
        PagedList<Integer> list = new PagedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 500, 0, 10);

        assertFalse(list.canGoBack());
        assertTrue(list.canGoNext());
    }
}