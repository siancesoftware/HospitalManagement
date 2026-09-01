package com.siance.hm.lab.dto;

import java.util.UUID;

public record LabResponse(UUID id, String name, String location, boolean active, UUID hospitalId) {
}
