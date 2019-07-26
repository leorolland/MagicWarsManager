package net.magicwars.manager.db;

import java.time.Duration;

public interface AsyncCallback<T> {
	void callback(T output, Duration elapsed);
}
