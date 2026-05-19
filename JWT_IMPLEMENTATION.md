# JWT Authentication & Role-Based Security Implementation

## Overview

This document describes the JWT (JSON Web Token) authentication system and role-based access control (RBAC) implemented in the Mira-Mar hotel management platform.

## Architecture

### Backend (Spring Boot)

#### 1. **JwtTokenProvider** (`security/JwtTokenProvider.java`)
- Generates JWT tokens with user information (userId, role, username)
- Validates token signatures and expiration
- Provides methods to extract claims (userId, role, username) from tokens
- Uses HMAC-SHA512 algorithm for signing
- Token expiration: 24 hours (configurable)
- Refresh token expiration: 7 days (configurable)

#### 2. **JwtAuthenticationFilter** (`security/JwtAuthenticationFilter.java`)
- Intercepts all HTTP requests (except auth endpoints)
- Extracts JWT token from `Authorization: Bearer <token>` header
- Validates token and sets Spring Security context
- Stores userId, role, and username in request attributes for downstream use

#### 3. **SecurityConfig** (`config/SecurityConfig.java`)
- Configures Spring Security with stateless session management
- Enables method-level security with `@PreAuthorize` annotations
- Defines public vs. protected endpoints:
  - **Public**: `/auth/login`, `/auth/register`, `/auth/logout`, Swagger UI, H2 console
  - **ADMIN only**: `/admin/**`
  - **ADMIN/OPERATOR**: Most management endpoints
  - **All authenticated users**: Read operations for rooms, services, room types
  - **ADMIN/OPERATOR/CLIENT**: Reservations, chatbot

#### 4. **AuthController** (updated)
- `/auth/login`: Returns `JwtAuthenticationResponse` with access & refresh tokens
- `/auth/logout`: Stateless (no server-side session clearing needed)
- Tokens are stored client-side in localStorage

### Frontend (Angular)

#### 1. **AuthInterceptor** (updated)
- Intercepts all HTTP requests (except auth endpoints)
- Adds `Authorization: Bearer <token>` header if token exists in localStorage
- Automatically attaches JWT to every authenticated request

#### 2. **AuthService** (updated)
- Stores entire `LoginResponse` in localStorage under key `miramar_session`
- Provides helper methods:
  - `getToken()`: Retrieves access token
  - `getUserId()`: Retrieves user ID
  - `getRole()`: Retrieves user role
  - `isLoggedIn()`: Checks authentication status
- Automatically calls `setSession()` after successful login

#### 3. **Auth Models** (updated)
```typescript
interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  userId: number;
  role: string;
  username: string;
  expiresIn: number;
}
```

## Configuration

### JWT Properties (`application.properties`)
```properties
jwt.secret=miramar-hotel-jwt-secret-key-2024-super-secure-key-change-in-production
jwt.expiration=86400000          # 24 hours in milliseconds
jwt.refresh-expiration=604800000 # 7 days in milliseconds
```

**Important**: Change `jwt.secret` in production to a strong, unique value.

## Role-Based Access Control

### User Roles
1. **ADMIN**: Full system access, user management
2. **OPERATOR**: Room management, reservation management, service coordination
3. **CLIENT**: Create reservations, view own reservations, use chatbot

### Protected Endpoints

#### Admin Only
```
POST   /admin/**
GET    /admin/stats
DELETE /operator/* (Operator management)
```

#### Admin + Operator
```
POST   /rooms/**
PUT    /rooms/**
DELETE /rooms/**
POST   /roomtypes/**
PUT    /roomtypes/**
DELETE /roomtypes/**
POST   /services/**
PUT    /services/**
DELETE /services/**
PUT    /reservations/{id}/status
```

#### All Authenticated Users (with GET for everyone)
```
GET    /rooms/**
GET    /roomtypes/**
GET    /services/**
GET    /reservations/**
POST   /reservations/add
POST   /chatbot/ask
```

## Token Flow

### Login Process
1. Client sends `POST /auth/login` with username and password
2. Backend validates credentials (checks Client, Operator, Administrator tables)
3. Backend generates JWT token with userId, role, and username
4. Backend returns `JwtAuthenticationResponse` with accessToken, refreshToken, etc.
5. Client stores response in localStorage

### Authenticated Request Flow
1. Client adds `Authorization: Bearer <token>` header (via interceptor)
2. `JwtAuthenticationFilter` intercepts request
3. Filter validates token signature and expiration
4. If valid, sets Spring Security context with userId and role
5. `@PreAuthorize` annotations check role-based permissions
6. Request is processed or rejected based on authorization

### Logout Process
1. Client sends `POST /auth/logout`
2. Backend returns success (no session to clear on server)
3. Client clears localStorage and JWT token

## Migration from Cookie-Based Auth

### What Changed
- **Before**: Cookie-based with plaintext user ID
- **After**: JWT-based with encrypted token containing user data

### Key Differences
1. **Stateless**: No server-side session storage required
2. **Secure**: JWT is signed and can include expiration
3. **Scalable**: Works with multiple servers without session sharing
4. **Client Storage**: Token stored in localStorage, not cookies

### Breaking Changes
- Controllers that relied on `@CookieValue("user_session")` need updates
- Example: `ReservationController.addReservation()` now uses `SecurityContextHolder.getContext().getAuthentication()`

## Security Best Practices

1. **Secret Key**: Use a strong, unique secret in production (minimum 32 characters)
2. **HTTPS**: Always use HTTPS in production (JWT in Authorization header must be encrypted in transit)
3. **Token Expiration**: 24-hour access tokens balance security and user experience
4. **Refresh Tokens**: 7-day refresh tokens allow long-term sessions without exposing access token
5. **CORS**: Configured to allow `http://localhost:4200` (update for production domains)
6. **Same-Site**: Consider adding `Secure` and `SameSite` headers in production

## Troubleshooting

### "Invalid JWT token" Error
- Check that token hasn't expired
- Verify token isn't corrupted in localStorage
- Ensure secret key matches on frontend and backend

### "Missing or invalid Authorization header"
- Verify interceptor is properly registered in `app.module.ts`
- Check that token is stored in localStorage after login
- Ensure request includes Authorization header

### "Access is denied" (403)
- Verify user role matches `@PreAuthorize` requirements
- Check that role is correctly extracted from JWT
- Ensure authenticated user isn't a CLIENT trying to access ADMIN endpoints

## Files Modified/Created

### Backend
- ✅ `pom.xml` - Added Spring Security and JWT dependencies
- ✅ `application.properties` - JWT configuration
- ✅ `security/JwtTokenProvider.java` - Token generation/validation
- ✅ `security/JwtAuthenticationFilter.java` - Request interception
- ✅ `config/SecurityConfig.java` - Spring Security configuration
- ✅ `controller/dto/JwtAuthenticationResponse.java` - JWT response DTO
- ✅ `controller/AuthController.java` - Updated for JWT
- ✅ `controller/AdminController.java` - Added @PreAuthorize
- ✅ `controller/OperatorController.java` - Added @PreAuthorize
- ✅ `controller/RoomController.java` - Added @PreAuthorize
- ✅ `controller/RoomTypeController.java` - Added @PreAuthorize
- ✅ `controller/HotelServiceController.java` - Added @PreAuthorize
- ✅ `controller/ReservationController.java` - Updated for JWT + @PreAuthorize
- ✅ `controller/ChatbotController.java` - Added @PreAuthorize
- ✅ `controller/UserController.java` - Added @PreAuthorize

### Frontend
- ✅ `auth.interceptor.ts` - Updated for JWT header
- ✅ `auth.service.ts` - Token extraction and storage methods
- ✅ `auth.model.ts` - Updated LoginResponse interface

## Next Steps

1. **Test all endpoints** with various roles to ensure authorization works
2. **Implement refresh token logic** if long-lived sessions are needed
3. **Add password hashing** (currently plaintext - security risk!)
4. **Implement token blacklist** for revocation on logout
5. **Update frontend components** to handle 401/403 responses gracefully
6. **Change JWT secret** in production environment
