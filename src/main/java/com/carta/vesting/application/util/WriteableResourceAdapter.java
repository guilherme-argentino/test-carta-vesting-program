package com.carta.vesting.application.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

public class WriteableResourceAdapter implements WritableResource {
	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long lastModified() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public URL getURL() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getURI() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFile() throws IOException {
		// TODO Auto-generated method stub
		return File.createTempFile("xxx", "yyy");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Resource createRelative(String relativePath) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long contentLength() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return null;
	}
}