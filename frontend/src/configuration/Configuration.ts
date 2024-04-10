type Environment = {
  type: 'development' | 'production' | 'test'
  isDevelopment: boolean
  isProduction: boolean
}

export interface IAppConfiguration {
  environment: Environment
  apiBaseAddress: string
  applicationName: string
}

//For now left it like that
export function getConfiguration(): IAppConfiguration {
  const fromEnvVarsEnvironment = process.env.NODE_ENV
  return <IAppConfiguration>{
    environment: {
      type: fromEnvVarsEnvironment,
      isDevelopment: fromEnvVarsEnvironment == 'development',
      isProduction: fromEnvVarsEnvironment == 'production',
    },
    apiBaseAddress:
      process.env.REACT_APP_API_BASE ?? 'http://localhost:3000/api',
    applicationName: process.env.REACT_APP_APPLICATION_NAME ?? 'Nico AI App',
  }
}
