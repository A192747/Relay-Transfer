package com.example.transfer.entity;

import java.io.Serializable;

public record FileData(byte[] data, String fileName) implements Serializable {
}
