const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'

// Types based on OpenAPI schema
export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  username: string
  role: string
}

export interface GetClientResponse {
  id: number
  name: string
  phone: string
  createdAt: string
}

export interface CreateClientRequest {
  name: string
  phone: string
}

export interface UpdateClientRequest {
  name?: string
  phone?: string
}

export interface UpdateClientResponse {
  id: number
  name: string
  phone: string
}

export interface GetCourtResponse {
  id: number
  name: string
  description: string
}

export interface CreateCourtRequest {
  name: string
  description?: string
}

export interface UpdateCourtRequest {
  name?: string
  description?: string
}

export interface UpdateCourtResponse {
  id: number
  name: string
  description: string
}

export interface CreateCourtResponse {
  id: number
  name: string
  description: string
}

export interface GetReservationResponse {
  id: number
  clientId: number
  clientName: string
  courtId: number
  courtName: string
  statusId: number
  statusName: string
  startDatetime: string
  endDatetime: string
  createdBy: string
  notes: string
  createdAt: string
  reservationCode: string
}

export interface GetPublicReservationResponse {
  id: number
  courtId: number
  courtName: string
  statusId: number
  statusName: string
  startDatetime: string
  endDatetime: string
  reservationCode: string
}

export interface CreateReservationRequest {
  clientName: string
  clientPhone: string
  courtId: number
  statusId: number
  startDatetime: string
  endDatetime: string
  createdBy?: string
  notes?: string
}

export interface CreateReservationResponse {
  id: number
  clientName: string
  courtName: string
  statusName: string
  startDatetime: string
  endDatetime: string
  notes: string
  reservationCode: string
}

export interface UpdateReservationAdminRequest {
  courtId: number
  statusId: number
  startDatetime: string
  endDatetime: string
  notes?: string
}

export interface UpdateReservationClientRequest {
  courtId: number
  startDatetime: string
  endDatetime: string
  reservationCode: string
  notes?: string
}

export interface UpdateReservationResponse {
  id: number
  clientName: string
  courtName: string
  statusName: string
  startDatetime: string
  endDatetime: string
  notes: string
  reservationCode: string
}

export interface CancelReservationRequest {
  reservationCode: string
}

// API Error response type matching backend ErrorResponse
export interface ApiErrorResponse {
  message: string
  status: number
  timestamp: string
}

// Custom error class to handle API errors
export class ApiError extends Error {
  status: number
  timestamp: string

  constructor(errorResponse: ApiErrorResponse) {
    super(errorResponse.message)
    this.name = 'ApiError'
    this.status = errorResponse.status
    this.timestamp = errorResponse.timestamp
  }
}

// Helper function to get auth token
function getToken(): string | null {
  if (typeof window === 'undefined') return null
  return localStorage.getItem('token')
}

// Helper function to parse error response from backend
async function parseErrorResponse(response: Response): Promise<ApiErrorResponse> {
  try {
    const data = await response.json()
    // Backend returns { message, status, timestamp }
    return {
      message: data.message || data.error || `Error ${response.status}: ${response.statusText}`,
      status: data.status || response.status,
      timestamp: data.timestamp || new Date().toISOString()
    }
  } catch {
    return {
      message: `Error ${response.status}: ${response.statusText}`,
      status: response.status,
      timestamp: new Date().toISOString()
    }
  }
}

// Helper function to handle API response - throws ApiError if not ok
async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorData = await parseErrorResponse(response)
    throw new ApiError(errorData)
  }
  return response.json()
}

// Helper function to make authenticated requests
async function fetchWithAuth(url: string, options: RequestInit = {}): Promise<Response> {
  const token = getToken()
  const headers = new Headers(options.headers)
  
  if (token) {
    headers.set('Authorization', `Bearer ${token}`)
  }
  headers.set('Content-Type', 'application/json')
  
  return fetch(`${API_BASE_URL}${url}`, {
    ...options,
    headers,
  })
}

// Auth API
export const authApi = {
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })
    return handleResponse<LoginResponse>(response)
  },
}

// Clients API - All methods now throw ApiError with backend error messages
export const clientsApi = {
  getAll: async (): Promise<GetClientResponse[]> => {
    const response = await fetchWithAuth('/clients/all')
    return handleResponse<GetClientResponse[]>(response)
  },
  
  getById: async (id: number): Promise<GetClientResponse> => {
    const response = await fetchWithAuth(`/clients/${id}`)
    return handleResponse<GetClientResponse>(response)
  },
  
  create: async (data: CreateClientRequest): Promise<GetClientResponse> => {
    const response = await fetchWithAuth('/clients/create', {
      method: 'POST',
      body: JSON.stringify(data),
    })
    return handleResponse<GetClientResponse>(response)
  },
  
  update: async (id: number, data: UpdateClientRequest): Promise<UpdateClientResponse> => {
    const response = await fetchWithAuth(`/clients/update/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    })
    return handleResponse<UpdateClientResponse>(response)
  },
  
  delete: async (id: number): Promise<string> => {
    const response = await fetchWithAuth(`/clients/delete/${id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      const errorData = await parseErrorResponse(response)
      throw new ApiError(errorData)
    }
    return response.text()
  },
}

// Courts API - All methods now throw ApiError with backend error messages
export const courtsApi = {
  getAll: async (): Promise<GetCourtResponse[]> => {
    const response = await fetchWithAuth('/courts/all')
    return handleResponse<GetCourtResponse[]>(response)
  },
  
  getById: async (id: number): Promise<GetCourtResponse> => {
    const response = await fetchWithAuth(`/courts/${id}`)
    return handleResponse<GetCourtResponse>(response)
  },
  
  create: async (data: CreateCourtRequest): Promise<CreateCourtResponse> => {
    const response = await fetchWithAuth('/courts/create', {
      method: 'POST',
      body: JSON.stringify(data),
    })
    return handleResponse<CreateCourtResponse>(response)
  },
  
  update: async (id: number, data: UpdateCourtRequest): Promise<UpdateCourtResponse> => {
    const response = await fetchWithAuth(`/courts/update/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    })
    return handleResponse<UpdateCourtResponse>(response)
  },
  
  delete: async (id: number): Promise<string> => {
    const response = await fetchWithAuth(`/courts/delete/${id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      const errorData = await parseErrorResponse(response)
      throw new ApiError(errorData)
    }
    return response.text()
  },
}

// Reservations API - All methods now throw ApiError with backend error messages
export const reservationsApi = {
  getAll: async (): Promise<GetReservationResponse[]> => {
    const response = await fetchWithAuth('/reservations/all')
    return handleResponse<GetReservationResponse[]>(response)
  },
  
  getPublic: async (): Promise<GetPublicReservationResponse[]> => {
    const response = await fetch(`${API_BASE_URL}/reservations/public`)
    return handleResponse<GetPublicReservationResponse[]>(response)
  },
  
  getById: async (id: number): Promise<GetReservationResponse> => {
    const response = await fetchWithAuth(`/reservations/${id}`)
    return handleResponse<GetReservationResponse>(response)
  },
  
  create: async (data: CreateReservationRequest): Promise<CreateReservationResponse> => {
    const response = await fetchWithAuth('/reservations/create', {
      method: 'POST',
      body: JSON.stringify(data),
    })
    return handleResponse<CreateReservationResponse>(response)
  },
  
  updateAdmin: async (id: number, data: UpdateReservationAdminRequest): Promise<UpdateReservationResponse> => {
    const response = await fetchWithAuth(`/reservations/update/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    })
    return handleResponse<UpdateReservationResponse>(response)
  },
  
  updateClient: async (id: number, data: UpdateReservationClientRequest): Promise<UpdateReservationResponse> => {
    const response = await fetchWithAuth(`/reservations/update-client/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    })
    return handleResponse<UpdateReservationResponse>(response)
  },
  
  cancel: async (id: number, data: CancelReservationRequest): Promise<GetReservationResponse> => {
    const response = await fetchWithAuth(`/reservations/cancel/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    })
    return handleResponse<GetReservationResponse>(response)
  },
  
  delete: async (id: number): Promise<string> => {
    const response = await fetchWithAuth(`/reservations/delete/${id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      const errorData = await parseErrorResponse(response)
      throw new ApiError(errorData)
    }
    return response.text()
  },
}
