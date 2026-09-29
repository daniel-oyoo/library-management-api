# Changelog

## [0.2.0] - 2026-09-29

### Added
- Integrated Google Books API as the primary search source via Spring Cloud OpenFeign.
- Added automatic fallback to the existing local in-memory/library data when Google is unavailable or returns no results.
- Added `source` metadata to book responses to indicate whether the record came from Google or the local library.
- Added resilience configuration for Feign and circuit-breaking around Google Books calls.

### Changed
- `GET /api/books/search` supports `source=auto|google|local`.
- Default project version bumped to `0.2.0`.

### Notes
- Existing local CRUD endpoints remain backward compatible.
