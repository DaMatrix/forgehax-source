package com.matt.forgehax.util.container.lists;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.util.NonNullList;

public class BetterNonNullList<E> extends NonNullList<E> {
	public BetterNonNullList()	{
        super(new ArrayList(), null);
    }
}
