export interface IAppConfiguration {
  apiBaseAddress: string
  applicationName: string
}

export function getConfiguration(): IAppConfiguration {
  const fromEnvVarsEnvironment: ImportMetaEnv = import.meta.env
  return {
    apiBaseAddress:
      fromEnvVarsEnvironment.VITE_APP_API_BASE ?? 'http://localhost:8080/api',
    applicationName: fromEnvVarsEnvironment.VITE_APP_TITLE ?? 'Nico AI App',
  }
}
