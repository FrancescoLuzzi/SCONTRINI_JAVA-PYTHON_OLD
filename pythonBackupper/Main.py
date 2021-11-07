import json
import errno
from Functions_swag import *
import sys

main_path = "/path/dir/out"
main_file = "Scontrini.txt"
json_file = "Scontrini.json"

saver = Saver(main_path, main_file, json_file)
saver.reader()
if saver.notZero():
    print(saver)
    if sys.argv.__len__() > 1 and str(sys.argv[1]) == "SaveLauncher":
        dizionario = dictioner(saver.main_lines)
        saver.jsoner(dizionario)
    else:
        risposta = input("vuoi sincronizzare il file json?yes/no\n").lower()
        while True:
            if risposta == "yes" or risposta == "no":
                break
            print("\r yes or no?")
            risposta = input().lower()
        if risposta == "yes":
            dizionario = dictioner(saver.main_lines)
            saver.jsoner(dizionario)

        opzioni = [
            "Sincronizza file originale",
            "Salva su nuovo file",
            "Salva su DB sql",
            "Non fare nulla",
        ]
        print("Cosa vuoi fare?")
        for x, opz in enumerate(opzioni):
            print(x + 1, ". ", opz)
        while True:
            try:
                opz = int(input())
                if opz > 0 and opz <= opzioni.__len__():
                    saver.run_option(opz)
                    saver.finalize()
                    break
                else:
                    print("numero non valido")
            except Exception as e:
                print("scrivi un numero!")
