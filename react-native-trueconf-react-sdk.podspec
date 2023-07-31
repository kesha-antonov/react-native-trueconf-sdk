require 'json'
package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.description  = package['description']
  s.homepage     = package['repository']['url']
  s.license      = "MIT"
  s.license      = { :type => "MIT" }
  s.author       = { "author" => "Kesha Antonov <innokenty.longway@gmail.com>" }
  s.platform     = :ios, "12.4"
  s.source       = { :git => "https://github.com/kesha-antonov/react-native-trueconf-react-sdk.git", :tag => "v#{s.version}" }
  s.source_files = "**/*.{h,m,swift}"
  s.requires_arc = true


  s.dependency "React"
end
