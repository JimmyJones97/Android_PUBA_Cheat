// Copyright (c) 2002-2004 Brian Wellington (bwelling@xbill.org)

package com.tsml.hkl.DNS;

import java.io.*;

/**
 * An exception thrown when unable to parse text.
 *
 * @author Brian Wellington
 */

public class TextParseException extends IOException {

public
TextParseException() {
	super();
}

public
TextParseException(String s) {
	super(s);
}

}
