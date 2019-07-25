package com.freebuildserver.plotManager.db;

import java.time.Duration;

public interface AsyncCallback<T> {
	void callback(T output, Duration elapsed);
}
